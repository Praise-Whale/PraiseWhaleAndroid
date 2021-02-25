package com.example.praisewhale.home.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.praisewhale.CollectionImpl
import com.example.praisewhale.R
import com.example.praisewhale.SettingActivity
import com.example.praisewhale.databinding.FragmentHomeBinding
import com.example.praisewhale.home.data.ResponseHomePraise
import com.example.praisewhale.home.ui.dialog.HomeDialogDoneFragment
import com.example.praisewhale.home.ui.dialog.HomeDialogUndoneFragment
import com.example.praisewhale.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class HomeFragment : Fragment() {

    private var _viewBinding: FragmentHomeBinding? = null
    private val viewBinding get() = _viewBinding!!

    private var currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()
    private var currentMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1).toString()
    private var currentDate = Calendar.getInstance().get(Calendar.DATE).toString()
    private var currentYMD = currentYear + currentMonth + currentDate

    private val sharedPreferences = MyApplication.mySharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentHomeBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    // ui 작업 수행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setDefaultInfo()
        setPraiseStatusDefaultView()
    }

    override fun onResume() {
        super.onResume()
        checkLastGetPraiseDate()
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
                0 -> setPraiseStatusDefaultView() // 아무 버튼도 누르지 않았을 경우 기본 뷰 설정
                else -> setPraiseStatusResultView(lastGetPraiseStatus) // 칭찬 상태에 따라 뷰 설정
            }
        }
    }

    private fun setPraiseStatusDefaultView() {
        viewBinding.apply {
            buttonNegative.setVisible()
            buttonPositive.setVisible()
            buttonPraiseStatus.setInvisible()
            textViewPraiseStatusDescription.setInvisible()
            textViewPraiseStatus.setInvisible()
            imageViewDolphin.setImageResource(R.drawable.main_img_whale)
        }
    }

    private fun setPraiseStatusResultView(lastGetPraiseStatus: String) {
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
                    textViewPraiseDescription.text = "완료한 칭찬은 카드서랍에서 확인할 수 있어요!"
                }
                "undone" -> {
                    imageViewDolphin.setImageResource(R.drawable.main_img_whale_fail)
                    buttonPraiseStatus.setContextCompatBackgroundTintList(R.color.very_light_pink)
                    textViewPraiseStatus.setContextCompatTextColor(R.color.black)
                    textViewPraiseStatus.text = "미완료"
                    textViewPraiseDescription.text = "내일은 꼭 칭찬해서\n" + "고래를 춤추게 해요!"
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
            400 -> {
                // todo - 토큰 값 갱신 후 재요청
                Log.d("TAG", "handlePraiseDataStatusCode: 토큰 값 갱신")
                getServerPraiseData()
            }
            // todo - 각 에러 코드별 처리..
            else -> {
                Log.d(
                    "TAG",
                    "handlePraiseDataStatusCode: ${response.code()}, ${response.message()}"
                )
            }
        }
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
        val dialogDone = HomeDialogDoneFragment.CustomDialogBuilder().getPraiseIndex(praiseIndex).create()
        dialogDone.show(parentFragmentManager, dialogDone.tag)
    }

    private fun showDialogUndone() {
        val dialogUndone = HomeDialogUndoneFragment.CustomDialogBuilder().create()
        dialogUndone.show(parentFragmentManager, dialogUndone.tag)
        sharedPreferences.setValue(LAST_PRAISE_STATUS, "undone")
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