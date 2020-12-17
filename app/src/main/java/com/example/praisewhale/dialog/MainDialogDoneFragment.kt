package com.example.praisewhale.dialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import com.example.praisewhale.CollectionImpl
import com.example.praisewhale.R
import com.example.praisewhale.ResponsePraiseTargetData
import com.example.praisewhale.databinding.DialogMainDoneBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainDialogDoneFragment : DialogFragment() {

    private var _dialogDoneViewBinding: DialogMainDoneBinding? = null
    private val dialogDoneViewBinding get() = _dialogDoneViewBinding!!

    private var praiseIndex: Int = 0
    private lateinit var listRecentPraiseTo: ArrayList<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _dialogDoneViewBinding = DialogMainDoneBinding.inflate(layoutInflater)
        return dialogDoneViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setRecentPraiseTo()
        setDialogBackground()
        dialogDoneViewBinding.imageButtonDelete.visibility = ImageButton.GONE
    }

    private fun setListeners() {
        dialogDoneViewBinding.apply {
            imageButtonClose.setOnClickListener(dialogDoneClickListener)
            editTextPraiseTo.addTextChangedListener(dialogTextWatcher)
            imageButtonDelete.setOnClickListener(dialogDoneClickListener)
            buttonRecentPraiseTo01.setOnClickListener(dialogDoneClickListener)
            buttonRecentPraiseTo02.setOnClickListener(dialogDoneClickListener)
            buttonRecentPraiseTo03.setOnClickListener(dialogDoneClickListener)
            buttonConfirm.setOnClickListener(dialogDoneClickListener)
        }
    }

    private fun setRecentPraiseTo() {
        getServerRecentPraiseTo()
        dialogDoneViewBinding.apply {
            buttonRecentPraiseTo01.text = listRecentPraiseTo[0]
            buttonRecentPraiseTo02.text = listRecentPraiseTo[1]
            buttonRecentPraiseTo03.text = listRecentPraiseTo[2]
        }
    }

    private fun setDialogBackground() {
        val dialogWidth = resources.getDimensionPixelSize(R.dimen.dialog_main_width)
        dialog!!.window!!.setLayout(dialogWidth, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog!!.window!!.setBackgroundDrawableResource(R.drawable.background_rectangle_radius_15_stroke)
    }

    private fun getServerRecentPraiseTo() {
        listRecentPraiseTo = arrayListOf("김희빈", "김영민", "안나영")
    }

    private val dialogDoneClickListener = View.OnClickListener {
        when (it.id) {
            dialogDoneViewBinding.imageButtonClose.id -> {
                dialog!!.dismiss()
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
                saveServerPraiseData(dialogDoneViewBinding.editTextPraiseTo.text.toString())
                showDialogResult()
                dialog!!.dismiss()
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

    private fun showDialogResult() {
        val dialogDoneResult = MainDialogDoneResultFragment.CustomDialogBuilder().create()
        dialogDoneResult.show(fragmentManager!!, dialogDoneResult.tag)
    }

    private val dialogTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (dialogDoneViewBinding.editTextPraiseTo.text.toString() == "") {
                dialogDoneViewBinding.imageButtonDelete.visibility = ImageButton.GONE
            } else {
                dialogDoneViewBinding.imageButtonDelete.visibility = ImageButton.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _dialogDoneViewBinding = null
    }

    class CustomDialogBuilder {
        private val dialog = MainDialogDoneFragment()

        fun getPraiseIndex(praiseIndex: Int): CustomDialogBuilder {
            dialog.praiseIndex = praiseIndex
            return this
        }

        fun create(): MainDialogDoneFragment {
            return dialog
        }
    }
}