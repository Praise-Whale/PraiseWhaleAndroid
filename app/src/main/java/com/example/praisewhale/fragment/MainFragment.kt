package com.example.praisewhale.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.example.praisewhale.CollectionImpl
import com.example.praisewhale.R
import com.example.praisewhale.ResponseHomeData
import com.example.praisewhale.ResponsePraiseTargetData
import com.example.praisewhale.databinding.DialogMainDoneBinding
import com.example.praisewhale.databinding.DialogMainDoneResultBinding
import com.example.praisewhale.databinding.DialogMainUndoneBinding
import com.example.praisewhale.databinding.FragmentMainBinding
import com.example.praisewhale.util.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates


class MainFragment : Fragment() {

    private var _mainViewBinding: FragmentMainBinding? = null
    private var _dialogDoneViewBinding: DialogMainDoneBinding? = null
    private var _dialogDoneResultViewBinding: DialogMainDoneResultBinding? = null
    private var _dialogUndoneViewBinding: DialogMainUndoneBinding? = null

    private val mainViewBinding get() = _mainViewBinding!!
    private val dialogDoneViewBinding get() = _dialogDoneViewBinding!!
    private val dialogDoneResultViewBinding get() = _dialogDoneResultViewBinding!!
    private val dialogUndoneViewBinding get() = _dialogUndoneViewBinding!!

    private lateinit var dialogDone: AlertDialog
    private lateinit var dialogDoneResult: AlertDialog
    private lateinit var dialogUndone: AlertDialog

    private var praiseIndex by Delegates.notNull<Int>()
    private val sharedPreferencesKey = "CountNegative"
    private lateinit var sharedPreferencesValue: String


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
        getServerPraiseData()
    }

    private fun setListeners() {
        mainViewBinding.buttonPositive.setOnClickListener(fragmentClickListener)
        mainViewBinding.buttonNegative.setOnClickListener(fragmentClickListener)
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
        _dialogDoneViewBinding = DialogMainDoneBinding.inflate(layoutInflater)
        dialogDoneViewBinding.apply {
            imageButtonClose.setOnClickListener(dialogDoneClickListener)
            imageButtonDelete.setOnClickListener(dialogDoneClickListener)
            buttonRecentPraiseTo01.setOnClickListener(dialogDoneClickListener)
            buttonRecentPraiseTo02.setOnClickListener(dialogDoneClickListener)
            buttonRecentPraiseTo03.setOnClickListener(dialogDoneClickListener)
            buttonConfirm.setOnClickListener(dialogDoneClickListener)
        }
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogWidth = resources.getDimensionPixelSize(R.dimen.dialog_main_width)

        dialogDone = dialogBuilder.setView(dialogDoneViewBinding.root).create()
        dialogDone.apply {
            show()
            window!!.setLayout(dialogWidth, WindowManager.LayoutParams.WRAP_CONTENT)
            window!!.setBackgroundDrawableResource(R.drawable.background_rectangle_radius_15_stroke)
        }
    }

    private fun showDialogDoneResult() {
        _dialogDoneResultViewBinding = DialogMainDoneResultBinding.inflate(layoutInflater)
        dialogDoneResultViewBinding.buttonConfirm.setOnClickListener(dialogDoneResultClickListener)

        val dialogBuilder = AlertDialog.Builder(context)
        val dialogWidth = resources.getDimensionPixelSize(R.dimen.dialog_main_width)

        dialogDoneResult = dialogBuilder.setView(dialogDoneResultViewBinding.root).create()
        dialogDoneResult.apply {
            show()
            window!!.setLayout(dialogWidth, WindowManager.LayoutParams.WRAP_CONTENT)
            window!!.setBackgroundDrawableResource(R.drawable.background_rectangle_radius_15_stroke)
        }
    }

    private fun showDialogUndone() {
        _dialogUndoneViewBinding = DialogMainUndoneBinding.inflate(layoutInflater)
        dialogUndoneViewBinding.buttonConfirm.setOnClickListener(dialogUndoneClickListener)

        sharedPreferencesValue = MyApplication.mySharedPreferences.getValue(sharedPreferencesKey, "")
        setNegativeDialog(sharedPreferencesValue)
        Log.d("TAG", "sharedPreferences: $sharedPreferencesValue")

        val dialogBuilder = AlertDialog.Builder(context)
        val dialogWidth = resources.getDimensionPixelSize(R.dimen.dialog_main_width)

        dialogUndone = dialogBuilder.setView(dialogUndoneViewBinding.root).create()
        dialogUndone.apply {
            show()
            window!!.setLayout(dialogWidth, WindowManager.LayoutParams.WRAP_CONTENT)
            window!!.setBackgroundDrawableResource(R.drawable.background_rectangle_radius_15_stroke)
        }
    }

    private fun setNegativeDialog(sharedPreferencesValue: String) {
        when (sharedPreferencesValue.toInt()) {
            0 -> {
                dialogUndoneViewBinding.apply {
                    textViewTitle.text = "아쉽고래!"
                    textViewSubTitle.text = "내일은 꼭 칭찬해요!"
                    imageViewWhale.setImageResource(R.drawable.no_1_img_whale)
                }
            }
            1 -> {
                dialogUndoneViewBinding.apply {
                    textViewTitle.text = "춤추고 싶고래!"
                    textViewSubTitle.text = "칭찬으로 저를 춤추게 해주세요!"
                    imageViewWhale.setImageResource(R.drawable.no_2_img_whale)
                }
            }
            2 -> {
                dialogUndoneViewBinding.apply {
                    textViewTitle.text = "고래고래 소리지를고래!"
                    textViewSubTitle.text = "칭찬하는 습관을 가져봐요!"
                    imageViewWhale.setImageResource(R.drawable.no_3_img_whale)
                }
            }
        }
    }

    private fun saveServerPraiseData(target: String) {
        val call: Call<ResponsePraiseTargetData> =
            CollectionImpl.service.postUsers(ResponsePraiseTargetData(praiseIndex, target))

        call.enqueue(object : Callback<ResponsePraiseTargetData> {
            override fun onFailure(call: Call<ResponsePraiseTargetData>, t: Throwable) {
                Log.d("tag", t.localizedMessage!!)
            }

            override fun onResponse(
                call: Call<ResponsePraiseTargetData>,
                response: Response<ResponsePraiseTargetData>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let { it ->

                    }
                Log.d("tag", "onResponse: success")
            }
        })
    }

    private val dialogDoneClickListener = View.OnClickListener {
        when (it.id) {
            dialogDoneViewBinding.imageButtonClose.id -> {
                dialogDone.dismiss()
            }
            dialogDoneViewBinding.imageButtonDelete.id -> {
                dialogDoneViewBinding.editTextPraiseTo.setText("")
            }
            dialogDoneViewBinding.buttonRecentPraiseTo01.id -> {
                dialogDoneViewBinding.editTextPraiseTo.setText(
                    dialogDoneViewBinding.buttonRecentPraiseTo01.text
                )
            }
            dialogDoneViewBinding.buttonRecentPraiseTo02.id -> {
                dialogDoneViewBinding.editTextPraiseTo.setText(
                    dialogDoneViewBinding.buttonRecentPraiseTo02.text
                )
            }
            dialogDoneViewBinding.buttonRecentPraiseTo03.id -> {
                dialogDoneViewBinding.editTextPraiseTo.setText(
                    dialogDoneViewBinding.buttonRecentPraiseTo03.text
                )
            }
            dialogDoneViewBinding.buttonConfirm.id -> {
                //saveServerPraiseData(dialogDoneViewBinding.editTextPraiseTo.text.toString())
                showDialogDoneResult()
                dialogDone.dismiss()
            }
        }
    }

    private val dialogDoneResultClickListener = View.OnClickListener {
        when (it.id) {
            dialogDoneResultViewBinding.buttonConfirm.id -> {
                dialogDoneResult.dismiss()
            }
        }
    }

    private val dialogUndoneClickListener = View.OnClickListener {
        when (it.id) {
            dialogUndoneViewBinding.buttonConfirm.id -> {
                updateSharedPreferences()
                dialogUndone.dismiss()
            }
        }
    }

    private fun updateSharedPreferences() {
        when (sharedPreferencesValue.toInt()) {
            2 -> {
                MyApplication.mySharedPreferences.setValue(sharedPreferencesKey, "0")
            }
            else -> {
                MyApplication.mySharedPreferences.setValue(
                    sharedPreferencesKey, ((sharedPreferencesValue.toInt() + 1).toString())
                )
                sharedPreferencesValue =
                    MyApplication.mySharedPreferences.getValue(sharedPreferencesKey, "0")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setViewBindingNull()
    }

    private fun setViewBindingNull() {
        _mainViewBinding = null
        _dialogDoneViewBinding = null
        _dialogDoneResultViewBinding = null
        _dialogUndoneViewBinding = null
    }
}