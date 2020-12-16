package com.example.praisewhale.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.praisewhale.R
import com.example.praisewhale.databinding.DialogMainResultBinding
import com.example.praisewhale.util.MyApplication


class MainDialogUndoneFragment : DialogFragment() {

    private var _dialogUndoneViewBinding: DialogMainResultBinding? = null
    private val dialogUndoneViewBinding get() = _dialogUndoneViewBinding!!

    private val sharedPreferencesKey = "CountNegative"
    private lateinit var sharedPreferencesValue: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _dialogUndoneViewBinding = DialogMainResultBinding.inflate(layoutInflater)
        return dialogUndoneViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setDialogContents()
        setDialogBackground()
    }

    private fun setListeners() {
        dialogUndoneViewBinding.buttonConfirm.setOnClickListener(dialogDoneClickListener)
    }

    private fun setDialogContents() {
        sharedPreferencesValue =
            MyApplication.mySharedPreferences.getValue(sharedPreferencesKey, "")
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

    private fun setDialogBackground() {
        val dialogWidth = resources.getDimensionPixelSize(R.dimen.dialog_main_width)
        dialog!!.window!!.setLayout(dialogWidth, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog!!.window!!.setBackgroundDrawableResource(R.drawable.background_rectangle_radius_15_stroke)
    }

    private val dialogDoneClickListener = View.OnClickListener {
        when (it.id) {
            dialogUndoneViewBinding.buttonConfirm.id -> {
                updateSharedPreferences()
                dialog!!.dismiss()
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
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _dialogUndoneViewBinding = null
    }

    class CustomDialogBuilder {
        private val dialog = MainDialogUndoneFragment()

        fun create(): MainDialogUndoneFragment {
            return dialog
        }
    }
}