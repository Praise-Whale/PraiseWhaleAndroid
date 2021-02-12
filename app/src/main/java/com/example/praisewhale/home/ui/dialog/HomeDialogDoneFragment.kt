package com.example.praisewhale.home.ui.dialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.praisewhale.CollectionImpl
import com.example.praisewhale.R
import com.example.praisewhale.databinding.DialogHomeDoneBinding
import com.example.praisewhale.home.data.RequestPraiseDone
import com.example.praisewhale.home.data.ResponseDonePraise
import com.example.praisewhale.home.data.ResponseRecentPraiseTo
import com.example.praisewhale.util.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeDialogDoneFragment : DialogFragment() {

    private var _dialogDoneViewBinding: DialogHomeDoneBinding? = null
    private val dialogDoneViewBinding get() = _dialogDoneViewBinding!!

    lateinit var praiseId: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _dialogDoneViewBinding = DialogHomeDoneBinding.inflate(layoutInflater)
        return dialogDoneViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setDialogBackground()
        setDefaultVisibility()
        getServerRecentPraiseTo()
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

    private fun setDialogBackground() {
        val dialogWidth = resources.getDimensionPixelSize(R.dimen.dialog_main_width)
        dialog!!.window!!.setLayout(dialogWidth, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog!!.window!!.setBackgroundDrawableResource(R.drawable.background_rectangle_radius_15_stroke)
    }

    private fun setDefaultVisibility() {
        dialogDoneViewBinding.apply {
            imageButtonDelete.visibility = ImageButton.GONE
            textViewRecentPraiseTo.visibility = TextView.GONE
            buttonRecentPraiseTo01.visibility = Button.GONE
            buttonRecentPraiseTo02.visibility = Button.GONE
            buttonRecentPraiseTo03.visibility = Button.GONE
        }
    }

    private fun getServerRecentPraiseTo() {
        val call: Call<ResponseRecentPraiseTo> = CollectionImpl.service.getRecentPraiseTo(
            MyApplication.mySharedPreferences.getValue("token", "")
        )
        call.enqueue(object : Callback<ResponseRecentPraiseTo> {
            override fun onFailure(call: Call<ResponseRecentPraiseTo>, t: Throwable) {
                Log.d("tag", t.localizedMessage!!)
            }
            override fun onResponse(
                call: Call<ResponseRecentPraiseTo>,
                response: Response<ResponseRecentPraiseTo>
            ) {
                if (response.isSuccessful) setRecentPraiseTo(response.body()!!.data)
                else handleRecentPraiseToStatusCode(response.body()!!)
            }
        })
    }

    private fun setRecentPraiseTo(listRecentPraiseTo: List<ResponseRecentPraiseTo.Name>) {
        Log.d("TAG", "setRecentPraiseTo: ${listRecentPraiseTo.size}")
        when (listRecentPraiseTo.size) {
            1 -> {
                dialogDoneViewBinding.apply {
                    textViewRecentPraiseTo.visibility = TextView.VISIBLE
                    buttonRecentPraiseTo01.visibility = Button.VISIBLE
                    buttonRecentPraiseTo01.text = listRecentPraiseTo[0].name
                }
            }
            2 -> {
                dialogDoneViewBinding.apply {
                    textViewRecentPraiseTo.visibility = TextView.VISIBLE
                    buttonRecentPraiseTo01.visibility = Button.VISIBLE
                    buttonRecentPraiseTo01.text = listRecentPraiseTo[0].name
                    buttonRecentPraiseTo02.visibility = Button.VISIBLE
                    buttonRecentPraiseTo02.text = listRecentPraiseTo[1].name
                }
            }
            3 -> {
                dialogDoneViewBinding.apply {
                    textViewRecentPraiseTo.visibility = TextView.VISIBLE
                    buttonRecentPraiseTo01.visibility = Button.VISIBLE
                    buttonRecentPraiseTo01.text = listRecentPraiseTo[0].name
                    buttonRecentPraiseTo02.visibility = Button.VISIBLE
                    buttonRecentPraiseTo02.text = listRecentPraiseTo[1].name
                    buttonRecentPraiseTo03.visibility = Button.VISIBLE
                    buttonRecentPraiseTo03.text = listRecentPraiseTo[2].name
                }
            }
        }
    }

    private fun handleRecentPraiseToStatusCode(response: ResponseRecentPraiseTo) {
        when (response.status) {
            400 -> {
                // todo - 토큰 값 갱신 후 재요청
                Log.d("TAG", "handleStatusCode: 토큰 값 갱신")
                getServerRecentPraiseTo()
            }
            // todo - 각 에러 코드별 처리..
            else -> {
                Log.d("TAG", "handleStatusCode: ${response.status}, ${response.message}")
            }
        }
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
            }
        }
    }

    private fun saveServerPraiseData(target: String) {
        val call: Call<ResponseDonePraise> = CollectionImpl.service.postPraiseDone(
            MyApplication.mySharedPreferences.getValue("token", ""),
            praiseId,
            RequestPraiseDone(target)
        )
        call.enqueue(object : Callback<ResponseDonePraise> {
            override fun onFailure(call: Call<ResponseDonePraise>, t: Throwable) {
                Log.d("tag", t.localizedMessage!!)
            }

            override fun onResponse(
                call: Call<ResponseDonePraise>,
                response: Response<ResponseDonePraise>
            ) {
                if (response.isSuccessful) {
                    checkLevelUp(response.body()!!.data)
                    showResultDialog()
                    dialog!!.dismiss()
                }
                else handleSaveServerPraiseStatusCode(response, target)
            }
        })
    }

    private fun checkLevelUp(response: ResponseDonePraise.Data) {
        if (response.isLevelUp) Toast.makeText(
            requireContext(), "레벨업 되었어요! 고래를 확인해보세요!", Toast.LENGTH_SHORT
        ).show()
    }

    private fun handleSaveServerPraiseStatusCode(response: Response<ResponseDonePraise>, target: String) {
        when (response.code()) {
            400 -> {
                // todo - 토큰 값 갱신 후 재요청
                // saveServerPraiseData(target)
            }
            else -> { // todo - 각 에러 코드별 처리..
                Log.d("TAG", "handleStatusCode: ${response.code()}, ${response.message()}")
            }
        }
    }

    private fun showResultDialog() {
        val dialogDoneResult = HomeDialogDoneResultFragment.CustomDialogBuilder().create()
        dialogDoneResult.show(parentFragmentManager, dialogDoneResult.tag)
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
        private val dialog = HomeDialogDoneFragment()

        fun getPraiseIndex(praiseId: String): CustomDialogBuilder {
            dialog.praiseId = praiseId
            return this
        }

        fun create(): HomeDialogDoneFragment {
            return dialog
        }
    }
}