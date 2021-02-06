package com.example.praisewhale.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.praisewhale.CollectionImpl
import com.example.praisewhale.LevelInfoActivity
import com.example.praisewhale.SettingActivity
import com.example.praisewhale.data.home.ResponseHomePraise
import com.example.praisewhale.databinding.*
import com.example.praisewhale.dialog.MainDialogDoneFragment
import com.example.praisewhale.dialog.MainDialogUndoneFragment
import com.example.praisewhale.util.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainFragment : Fragment() {

    private var _mainViewBinding: FragmentMainBinding? = null
    private val mainViewBinding get() = _mainViewBinding!!
    lateinit var praiseIndex: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _mainViewBinding = FragmentMainBinding.inflate(layoutInflater)
        return mainViewBinding.root
    }

    // ui 작업 수행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUserInfo()
        setListeners()
        setCurrentDate()
        getServerPraiseData()
    }

    private fun setUserInfo() {
        val userName = MyApplication.mySharedPreferences.getValue("nickName", "")
        mainViewBinding.textViewUserName.text = userName + "님을 위한"
    }

    private fun setListeners() {
        mainViewBinding.buttonPositive.setOnClickListener(fragmentClickListener)
        mainViewBinding.buttonNegative.setOnClickListener(fragmentClickListener)
        mainViewBinding.imageButtonSettings.setOnClickListener(fragmentClickListener)
    }

    private fun setCurrentDate() {
        val calendar = Calendar.getInstance()
        val month = (calendar.get(Calendar.MONTH) + 1).toString()
        val date = calendar.get(Calendar.DATE).toShort()
        mainViewBinding.buttonDate.text = month + "월 " + date + "일"
    }

    private fun getServerPraiseData() {
        val call: Call<ResponseHomePraise> = CollectionImpl.service.getPraise(
            MyApplication.mySharedPreferences.getValue("token", "")
        )
        call.enqueue(object : Callback<ResponseHomePraise> {
            override fun onFailure(call: Call<ResponseHomePraise>, t: Throwable) {
                Log.d("tag", t.localizedMessage!!)
            }

            override fun onResponse(
                call: Call<ResponseHomePraise>,
                response: Response<ResponseHomePraise>
            ) {
                if (response.isSuccessful) {
                    val praiseData = response.body()!!.data
                    praiseIndex = praiseData.praiseId.toString()
                    mainViewBinding.textViewDailyPraise.text = praiseData.dailyPraise
                    mainViewBinding.textViewPraiseDescription.text = praiseData.praiseDescription
                } else handlePraiseDataStatusCode(response.body()!!)
            }
        })
    }

    private fun handlePraiseDataStatusCode(response: ResponseHomePraise) {
        when (response.status) {
            400 -> {
                // todo - 토큰 값 갱신 후 재요청
                Log.d("TAG", "handleStatusCode: 토큰 값 갱신")
                getServerPraiseData()
            }
            // todo - 각 에러 코드별 처리..
            else -> {
                Log.d("TAG", "handleStatusCode: ${response.status}, ${response.message}")
            }
        }
    }

    private val fragmentClickListener = View.OnClickListener {
        when (it.id) {
            mainViewBinding.buttonPositive.id -> {
                showDialogDone()
            }
            mainViewBinding.buttonNegative.id -> {
                showDialogUndone()
            }
            mainViewBinding.imageButtonSettings.id -> {
                // todo - setting 뷰로 이동
                val intent= Intent(context, SettingActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun showDialogDone() {
        val dialogDone = MainDialogDoneFragment.CustomDialogBuilder()
            .getPraiseIndex(praiseIndex)
            .create()
        dialogDone.show(parentFragmentManager, dialogDone.tag)
    }

    private fun showDialogUndone() {
        val dialogUndone = MainDialogUndoneFragment.CustomDialogBuilder().create()
        dialogUndone.show(parentFragmentManager, dialogUndone.tag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mainViewBinding = null
    }
}