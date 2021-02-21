package com.example.praisewhale.home.ui.dialog

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.praisewhale.CollectionImpl
import com.example.praisewhale.R
import com.example.praisewhale.databinding.DialogHomeDoneBinding
import com.example.praisewhale.home.adapter.RecentPraiseToAdapter
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
    private var praiseId: Int = 0
    private lateinit var recentPraiseToList: List<ResponseRecentPraiseTo.Name>


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
        getServerRecentPraiseTo()
        setDefaultVisibility()
        setRecyclerView()
    }

    private fun setListeners() {
        dialogDoneViewBinding.apply {
            imageButtonClose.setOnClickListener(dialogDoneClickListener)
            editTextPraiseTo.addTextChangedListener(dialogTextWatcher)
            imageButtonDelete.setOnClickListener(dialogDoneClickListener)
            buttonConfirm.setOnClickListener(dialogDoneClickListener)
        }
    }

    private fun setDialogBackground() {
        val dialogWidth = resources.getDimensionPixelSize(R.dimen.dialog_main_width)
        dialog!!.window!!.setLayout(dialogWidth, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog!!.window!!.setBackgroundDrawableResource(R.drawable.background_rectangle_radius_15_stroke)
    }

    private fun setRecyclerView() {
        dialogDoneViewBinding.recyclerViewRecentPraiseTo.apply {
            adapter = RecentPraiseToAdapter(recentPraiseToList)
            addOnScrollListener(dialogDoneScrollListener)
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
                when (response.isSuccessful) {
                    true -> recentPraiseToList = response.body()!!.data
                    false -> handleRecentPraiseToStatusCode(response)
                }
            }
        })
    }

    private fun handleRecentPraiseToStatusCode(response: Response<ResponseRecentPraiseTo>) {
        when (response.code()) {
            400 -> {
                // todo - 토큰 값 갱신 후 재요청
                Log.d("TAG", "handleStatusCode: 토큰 값 갱신")
                getServerRecentPraiseTo()
            }
            // todo - 각 에러 코드별 처리..
            else -> {
                Log.d("TAG", "handleStatusCode: ${response.code()}, ${response.message()}")
            }
        }
    }

    private fun setDefaultVisibility() {
        recentPraiseToList = listOf(
            ResponseRecentPraiseTo.Name("김송현"),
            ResponseRecentPraiseTo.Name("남궁선규남궁"),
            ResponseRecentPraiseTo.Name("최윤소")
        )


        dialogDoneViewBinding.apply {
            imageButtonDelete.visibility = ImageButton.GONE
            textViewRecentPraiseToTitle.visibility = when (recentPraiseToList.size) {
                0 -> TextView.GONE
                else -> TextView.VISIBLE
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
            dialogDoneViewBinding.buttonConfirm.id -> {
//                saveServerPraiseData(dialogDoneViewBinding.editTextPraiseTo.text.toString())
                showResultDialog(true)
                dialog!!.dismiss()
            }
        }
    }

    private val dialogDoneScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
            val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
            updateBlurBox(firstVisibleItemPosition, lastVisibleItemPosition)
        }
    }

    private fun updateBlurBox(firstVisibleItemPosition: Int, lastVisibleItemPosition: Int) {
        if (firstVisibleItemPosition == 0) {
            dialogDoneViewBinding.imageViewBlurBoxLeft.fadeOut()
            dialogDoneViewBinding.imageViewBlurBoxRight.fadeIn()
        }
        if (lastVisibleItemPosition == 2) {
            dialogDoneViewBinding.imageViewBlurBoxLeft.fadeIn()
            dialogDoneViewBinding.imageViewBlurBoxRight.fadeOut()
        } else {
            dialogDoneViewBinding.imageViewBlurBoxLeft.fadeIn()
            dialogDoneViewBinding.imageViewBlurBoxRight.fadeIn()
        }
    }

    private fun View.fadeIn() {
        if (visibility == View.INVISIBLE) {
            visibility = View.VISIBLE
            alpha = 0f
            animate()
                .alpha(1f)
                .setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong())
                .setListener(null)
        }
    }

    private fun View.fadeOut() {
        if (visibility == View.VISIBLE) {
            alpha = 1f
            animate()
                .alpha(0f)
                .setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        visibility = View.INVISIBLE
                    }
                })
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
                when (response.isSuccessful) {
                    true -> {
                        showResultDialog(true)
                        dialog!!.dismiss()
                    }
                    false -> handleSaveServerPraiseStatusCode(response, target)
                }
            }
        })
    }

    private fun handleSaveServerPraiseStatusCode(
        response: Response<ResponseDonePraise>,
        target: String
    ) {
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

    private fun showResultDialog(isLevelUp: Boolean) {
        val dialogDoneResult = HomeDialogDoneResultFragment.CustomDialogBuilder()
            .getLevelUpStatus(isLevelUp)
            .create()
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

        fun getPraiseIndex(praiseId: Int): CustomDialogBuilder {
            dialog.praiseId = praiseId
            return this
        }

        fun create(): HomeDialogDoneFragment {
            return dialog
        }
    }
}