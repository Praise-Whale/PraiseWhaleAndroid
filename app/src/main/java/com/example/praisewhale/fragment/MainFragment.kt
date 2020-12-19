package com.example.praisewhale.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.praisewhale.CollectionImpl
import com.example.praisewhale.ResponseHomeData
import com.example.praisewhale.databinding.*
import com.example.praisewhale.dialog.MainDialogDoneFragment
import com.example.praisewhale.dialog.MainDialogUndoneFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.properties.Delegates


class MainFragment : Fragment() {

    private var _mainViewBinding: FragmentMainBinding? = null
    private val mainViewBinding get() = _mainViewBinding!!
    private var praiseIndex by Delegates.notNull<Int>()


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

        setListeners()
        setCurrentDate()
        getServerPraiseData()
    }

    private fun setListeners() {
        mainViewBinding.buttonPositive.setOnClickListener(fragmentClickListener)
        mainViewBinding.buttonNegative.setOnClickListener(fragmentClickListener)
    }

    private fun setCurrentDate() {
        val calendar = Calendar.getInstance()
        val month = (calendar.get(Calendar.MONTH) + 1).toString()
        val date = calendar.get(Calendar.DATE).toShort()
        mainViewBinding.buttonDate.text = month + "월 " + date + "일"
    }

    private fun getServerPraiseData() {
        val call: Call<ResponseHomeData> = CollectionImpl.service.getPraise()
        call.enqueue(object : Callback<ResponseHomeData> {

            override fun onFailure(call: Call<ResponseHomeData>, t: Throwable) {
                Log.d("tag", t.localizedMessage!!)
            }

            override fun onResponse(
                call: Call<ResponseHomeData>,
                response: Response<ResponseHomeData>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let {
                        praiseIndex = it.data.id
                        mainViewBinding.textViewDailyPraise.text = it.data.dailyPraise
                        mainViewBinding.textViewPraiseDescription.text = it.data.praiseDescription
                    }
                praiseIndex = 100 // 다이얼로그로 칭찬 인덱스가 넘어가는지 테스트하기 위한 임시 값
                Log.d("tag", "onResponse: success")
            }
        })
    }

    private val fragmentClickListener = View.OnClickListener {
        when (it.id) {
            mainViewBinding.buttonPositive.id -> {
                showDialogDone()
            }
            mainViewBinding.buttonNegative.id -> {
                showDialogUndone()
            }
        }
    }

    private fun showDialogDone() {
        val dialogDone = MainDialogDoneFragment.CustomDialogBuilder()
            .getPraiseIndex(praiseIndex)
            .create()
        dialogDone.show(fragmentManager!!, dialogDone.tag)
    }

    private fun showDialogUndone() {
        val dialogUndone = MainDialogUndoneFragment.CustomDialogBuilder().create()
        dialogUndone.show(fragmentManager!!, dialogUndone.tag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mainViewBinding = null
    }
}