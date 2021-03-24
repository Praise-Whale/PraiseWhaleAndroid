package com.sopt27.praisewhale.home.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.sopt27.praisewhale.CollectionImpl
import com.sopt27.praisewhale.MainActivity
import com.sopt27.praisewhale.R
import com.sopt27.praisewhale.SettingActivity
import com.sopt27.praisewhale.data.ResponseToken
import com.sopt27.praisewhale.databinding.FragmentHomeBinding
import com.sopt27.praisewhale.home.data.ResponseHomePraise
import com.sopt27.praisewhale.home.ui.dialog.HomeDialogDoneFragment
import com.sopt27.praisewhale.home.ui.dialog.HomeDialogUndoneFragment
import com.sopt27.praisewhale.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class HomeFragment : Fragment() {

    private var _viewBinding: FragmentHomeBinding? = null
    private val viewBinding get() = _viewBinding!!
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private lateinit var currentYear: String
    private lateinit var currentMonth: String
    private lateinit var currentDate: String
    private lateinit var currentYMD: String

    private val sharedPreferences = MyApplication.mySharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return viewBinding.root
    }

    // ui 작업 수행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setPraiseStatusDefaultVisibility()
        setOnBackPressedCallBack()
    }

    override fun onResume() {
        super.onResume()
        getCurrentYMD()
        setDefaultInfo()
        checkLastGetPraiseDate()
        if (!onBackPressedCallback.isEnabled) {
            onBackPressedCallback.isEnabled = true
        }
    }

    override fun onPause() {
        super.onPause()
        onBackPressedCallback.isEnabled = false
    }

    private fun setListeners() {
        viewBinding.apply {
            fragmentClickListener.let {
                buttonPositive.setOnClickListener(it)
                buttonNegative.setOnClickListener(it)
                imageButtonSettings.setOnClickListener(it)
            }
        }
    }

    private fun setDefaultInfo() {
        val userName = sharedPreferences.getValue("nickName", "")
        viewBinding.textViewUserName.text = userName + "님을 위한"
        viewBinding.buttonDate.text = "${currentMonth}월 ${currentDate}일"
    }

    private fun getCurrentYMD() {
        currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()
        currentMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1).toString()
        currentDate = Calendar.getInstance().get(Calendar.DATE).toString()
        currentYMD = currentYear + currentMonth + currentDate
    }

    private fun checkLastGetPraiseDate() {
        // sharedPreferences에 저장된 마지막 통신 날짜와 현재 날짜를 비교
        when (sharedPreferences.getValue(LAST_PRAISE_DATE, "")) {
            currentYMD -> setPraiseStatusView() // 날짜가 같다면 칭찬 상태에 따른 현재 뷰만 설정
            else -> getServerPraiseData() // 날짜가 다르다면 서버에 데이터 요청
        }
    }

    private fun setPraiseStatusView() {
        // sharedPreferences에 저장된 마지막 받아온 칭찬의 상태에 따라 뷰 설정
        // "" -> 아무 버튼도 누르지 않은 상태
        // "done" -> 했어요
        // "undone" -> 못했어요
        viewBinding.apply {
            textViewDailyPraise.text = sharedPreferences.getValue(LAST_PRAISE, "")
            textViewPraiseDescription.text = sharedPreferences.getValue(LAST_PRAISE_DESCRIPTION, "")

            val lastGetPraiseStatus = sharedPreferences.getValue(LAST_PRAISE_STATUS, "")
            when (lastGetPraiseStatus.length) {
                0 -> setPraiseStatusDefaultVisibility() // 아무 버튼도 누르지 않았을 경우 기본 뷰 설정
                else -> setPraiseStatusResultVisibility(lastGetPraiseStatus) // 칭찬 상태에 따라 뷰 설정
            }
        }
    }

    private fun setPraiseStatusDefaultVisibility() {
        viewBinding.apply {
            buttonNegative.setVisible()
            buttonPositive.setVisible()
            buttonPraiseStatus.setInvisible()
            textViewPraiseStatusDescription.setInvisible()
            textViewPraiseStatus.setInvisible()
            imageViewDolphin.setImageResource(R.drawable.main_img_whale)
        }
    }

    private fun setPraiseStatusResultVisibility(lastGetPraiseStatus: String) {
        setPraiseStatusResultButton(lastGetPraiseStatus)
        viewBinding.apply {
            buttonNegative.setInvisible()
            buttonPositive.setInvisible()
            buttonPraiseStatus.setVisible()
            textViewPraiseStatusDescription.setVisible()
            textViewPraiseStatus.setVisible()
        }
    }

    private fun setPraiseStatusResultButton(lastGetPraiseStatus: String) {
        viewBinding.apply {
            when (lastGetPraiseStatus) {
                "done" -> {
                    imageViewDolphin.setImageResource(R.drawable.main_img_whale_success)
                    buttonPraiseStatus.setContextCompatBackgroundTintList(R.color.dodger_blue_13)
                    textViewPraiseStatus.setContextCompatTextColor(R.color.dodger_blue)
                    textViewPraiseStatus.text = "완료"
                    textViewPraiseDescription.text = "완료한 칭찬은 카드서랍\n" + "에서 확인할 수 있어요!"
                }
                "undone" -> {
                    imageViewDolphin.setImageResource(R.drawable.main_img_whale_fail)
                    buttonPraiseStatus.setContextCompatBackgroundTintList(R.color.very_light_pink)
                    textViewPraiseStatus.setContextCompatTextColor(R.color.black)
                    textViewPraiseStatus.text = "미완료"
                    textViewPraiseDescription.text = "내일은 꼭 칭찬해서\n" + "고래를 춤 추게 해요!"
                }
            }
        }
    }

    private fun getServerPraiseData() {
        val call: Call<ResponseHomePraise> = CollectionImpl.service.getPraise(
            sharedPreferences.getValue("token", ""),
            sharedPreferences.getValue(LAST_PRAISE_INDEX, "0").toInt() + 1
        )
        call.enqueue(object : Callback<ResponseHomePraise> {
            override fun onFailure(call: Call<ResponseHomePraise>, t: Throwable) {
                Log.d("tag", t.localizedMessage!!)
            }

            override fun onResponse(
                call: Call<ResponseHomePraise>,
                response: Response<ResponseHomePraise>
            ) {
                when (response.isSuccessful) {
                    true -> setPraiseData(response.body()!!.data.homePraise)
                    false -> handlePraiseDataStatusCode(response)
                }
            }
        })
    }

    private fun setPraiseData(praiseData: ResponseHomePraise.Data.HomePraise) {
        saveLastGetPraiseData(praiseData)
        viewBinding.apply {
            textViewDailyPraise.text = praiseData.dailyPraise
            textViewPraiseDescription.text = praiseData.praiseDescription
        }
    }

    private fun saveLastGetPraiseData(praiseData: ResponseHomePraise.Data.HomePraise) {
        sharedPreferences.apply {
            setValue(LAST_PRAISE_DATE, currentYMD)
            setValue(LAST_PRAISE, praiseData.dailyPraise)
            setValue(LAST_PRAISE_STATUS, "")
            setValue(LAST_PRAISE_INDEX, praiseData.id.toString())
            setValue(LAST_PRAISE_DESCRIPTION, praiseData.praiseDescription)
        }
    }

    private fun handlePraiseDataStatusCode(response: Response<ResponseHomePraise>) {
        when (response.code()) {
            400 -> updateToken()
            else -> {
                Log.d("TAG", "handlePraiseDataStatusCode: ${response.code()}")
                return
            }
        }
    }

    private fun updateToken() {
        val call: Call<ResponseToken> = CollectionImpl.service.putRefreshToken(
            sharedPreferences.getValue("refreshToken", "")
        )
        call.enqueue(object : Callback<ResponseToken> {
            override fun onFailure(call: Call<ResponseToken>, t: Throwable) {
                Log.d("tag", t.localizedMessage!!)
            }

            override fun onResponse(
                call: Call<ResponseToken>,
                response: Response<ResponseToken>
            ) {
                when (response.isSuccessful) {
                    true -> {
                        saveNewTokenData(response.body()!!.data)
                        getServerPraiseData()
                    }
                    false -> {
                        Log.d("TAG", "HomeFragment - onResponse: error")
                        return
                    }
                }
            }
        })
    }

    private fun saveNewTokenData(tokenData: ResponseToken.Data) {
        sharedPreferences.apply {
            setValue("token", tokenData.accessToken)
            setValue("refreshToken", tokenData.refreshToken)
        }
    }

    private fun setOnBackPressedCallBack() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as MainActivity).showFinishToast()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    private val fragmentClickListener = View.OnClickListener {
        viewBinding.apply {
            when (it.id) {
                buttonPositive.id -> showDialogDone()
                buttonNegative.id -> showDialogUndone()
                imageButtonSettings.id -> setIntentToSettingActivity()
            }
        }
    }

    private fun showDialogDone() {
        val praiseIndex = sharedPreferences.getValue(LAST_PRAISE_INDEX, "").toInt()
        val dialogDone =
            HomeDialogDoneFragment.CustomDialogBuilder().setPraiseIndex(praiseIndex).create()
        dialogDone.isCancelable = false
        dialogDone.show(parentFragmentManager, dialogDone.tag)
    }

    private fun showDialogUndone() {
        val dialogUndone = HomeDialogUndoneFragment.CustomDialogBuilder().create()
        dialogUndone.isCancelable = false
        dialogUndone.show(parentFragmentManager, dialogUndone.tag)
    }

    private fun setIntentToSettingActivity() {
        val intent = Intent(context, SettingActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}