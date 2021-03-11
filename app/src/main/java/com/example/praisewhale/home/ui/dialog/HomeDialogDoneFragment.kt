package com.example.praisewhale.home.ui.dialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.praisewhale.CollectionImpl
import com.example.praisewhale.MainActivity
import com.example.praisewhale.R
import com.example.praisewhale.data.ResponseToken
import com.example.praisewhale.databinding.DialogHomeDoneBinding
import com.example.praisewhale.home.adapter.RecentPraiseToAdapter
import com.example.praisewhale.home.data.RequestPraiseDone
import com.example.praisewhale.home.data.ResponseDonePraise
import com.example.praisewhale.home.data.ResponseRecentPraiseTo
import com.example.praisewhale.home.ui.HomeFragment
import com.example.praisewhale.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class HomeDialogDoneFragment : DialogFragment(), RecentPraiseToClickListener {

    private var _viewBinding: DialogHomeDoneBinding? = null
    private val viewBinding get() = _viewBinding!!

    private var praiseId: Int = 0
    private val sharedPreferences = MyApplication.mySharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = DialogHomeDoneBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setDialogBackground()
        getServerRecentPraiseTo()
    }

    override fun onResume() {
        super.onResume()
        requireContext().showKeyboard()
        viewBinding.editTextPraiseTo.requestFocus()
    }

    override fun onPause() {
        super.onPause()
        requireContext().hideKeyboard()
    }

    private fun setListeners() {
        viewBinding.apply {
            imageButtonClose.setOnClickListener(fragmentClickListener)
            editTextPraiseTo.addTextChangedListener(dialogTextWatcher)
            editTextPraiseTo.setOnEditorActionListener(editTextActionListener)
            imageButtonDelete.setOnClickListener(fragmentClickListener)
            recyclerViewRecentPraiseTo.addOnScrollListener(dialogDoneScrollListener)
            buttonConfirm.setOnClickListener(fragmentClickListener)
        }
    }

    private fun setDialogBackground() {
        dialog!!.window!!.setBackgroundDrawableResource(R.drawable.background_rectangle_radius_15_stroke_2)
    }

    private fun getServerRecentPraiseTo() {
        val call: Call<ResponseRecentPraiseTo> = CollectionImpl.service.getRecentPraiseTo(
            sharedPreferences.getValue("token", "")
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
                    true -> setRecentPraiseToView(response.body()!!.data)
                    false -> handleRecentPraiseToStatusCode(response)
                }
            }
        })
    }

    private fun setRecentPraiseToView(recentPraiseToList: List<ResponseRecentPraiseTo.Name>) {
        viewBinding.apply {
            recyclerViewRecentPraiseTo.adapter =
                RecentPraiseToAdapter(recentPraiseToList, this@HomeDialogDoneFragment)
            when (recentPraiseToList.size) {
                0 -> textViewRecentPraiseToTitle.setInvisible()
                else -> textViewRecentPraiseToTitle.setVisible()
            }
        }
    }

    private fun handleRecentPraiseToStatusCode(response: Response<ResponseRecentPraiseTo>) {
        when (response.code()) {
            400 -> updateTokenForGetServerRecentPraiseTo()
            else -> {
                Log.d("TAG", "HomeDialogDoneFragment - handleStatusCode: ${response.code()}")
                return
            }
        }
    }

    private fun updateTokenForGetServerRecentPraiseTo() {
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
                        getServerRecentPraiseTo()
                    }
                    false -> {
                        Log.d("TAG", "HomeDialogDoneFragment - onResponse: error")
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

    private val dialogDoneScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            (recyclerView.layoutManager as LinearLayoutManager).apply {
                updateBlurBox(
                    findFirstCompletelyVisibleItemPosition(),
                    findLastCompletelyVisibleItemPosition()
                )
            }
        }
    }

    private fun updateBlurBox(
        firstCompletelyVisibleItemPosition: Int,
        lastCompletelyVisibleItemPosition: Int
    ) {
        viewBinding.apply {
            if (firstCompletelyVisibleItemPosition == 0) {
                imageViewBlurBoxLeft.fadeOut()
                imageViewBlurBoxRight.fadeIn()
            }
            if (lastCompletelyVisibleItemPosition == 2) {
                imageViewBlurBoxLeft.fadeIn()
                imageViewBlurBoxRight.fadeOut()
            } else {
                imageViewBlurBoxLeft.fadeIn()
                imageViewBlurBoxRight.fadeIn()
            }
        }
    }

    private val fragmentClickListener = View.OnClickListener {
        viewBinding.apply {
            when (it.id) {
                imageButtonClose.id -> dialog!!.dismiss()
                imageButtonDelete.id -> editTextPraiseTo.setText("")
                buttonConfirm.id -> saveServerPraiseData(editTextPraiseTo.text.toString())
            }
        }
    }

    private fun saveServerPraiseData(target: String) {
        val call: Call<ResponseDonePraise> = CollectionImpl.service.postPraiseDone(
            sharedPreferences.getValue("token", ""),
            praiseId,
            RequestPraiseDone(target, getCurrentDate())
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
                        dialog!!.dismiss()
                        updateHomeFragmentView()
                        showResultDialog(response.body()!!.data.isLevelUp)
                    }
                    false -> handleSaveServerPraiseStatusCode(response, target)
                }
            }
        })
    }

    private fun getCurrentDate(): String {
        val currentDate = Calendar.getInstance().time
        val pattern = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREAN)
        Log.d("TAG", "getCurrentDateForServer: ${pattern.format(currentDate)}")
        return pattern.format(currentDate)
    }

    private fun updateHomeFragmentView() {
        sharedPreferences.setValue(COUNT_UNDONE, "0")
        sharedPreferences.setValue(LAST_PRAISE_STATUS, "done")
        (activity as MainActivity).changeFragment(HomeFragment())
    }

    private fun handleSaveServerPraiseStatusCode(
        response: Response<ResponseDonePraise>,
        target: String
    ) {
        when (response.code()) {
            400 -> updateTokenForSaveServerPraiseData(target)
            else -> Log.d("TAG", "HomeDialogDoneFragment - handleStatusCode: ${response.code()}")
        }
    }

    private fun updateTokenForSaveServerPraiseData(target: String) {
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
                        saveServerPraiseData(target)
                    }
                    false -> Log.d("TAG", "HomeDialogDoneFragment - onResponse: error")
                }
            }
        })
    }

    private fun showResultDialog(isLevelUp: Boolean) {
        val dialogDoneResult = HomeDialogDoneResultFragment.CustomDialogBuilder()
            .getLevelUpStatus(isLevelUp)
            .create()
        dialogDoneResult.isCancelable = false
        dialogDoneResult.show(parentFragmentManager, dialogDoneResult.tag)
    }


    private val dialogTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            updateDeleteButtonVisibility(s!!)
            updateEditTextCurrentLength(s)
            updateConfirmButtonColor(s)
        }
    }

    private fun updateDeleteButtonVisibility(praiseTo: Editable) {
        viewBinding.apply {
            when (praiseTo.length) {
                0 -> imageButtonDelete.setInvisible()
                else -> imageButtonDelete.setVisible()
            }
        }
    }

    private fun updateEditTextCurrentLength(praiseTo: Editable) {
        viewBinding.textViewCurrentLength.apply {
            text = praiseTo.length.toString()
            when (praiseTo.length) {
                0 -> setContextCompatTextColor(R.color.brown_grey)
                else -> setContextCompatTextColor(R.color.brown)
            }
        }
    }

    private fun updateConfirmButtonColor(praiseTo: Editable) {
        viewBinding.buttonConfirm.apply {
            when (praiseTo.length) {
                0 -> setContextCompatBackgroundTintList(R.color.very_light_pink)
                else -> setContextCompatBackgroundTintList(R.color.sand_yellow)
            }
        }
    }

    private val editTextActionListener = TextView.OnEditorActionListener { _, actionId, _ ->
        when (actionId) {
            EditorInfo.IME_ACTION_DONE -> {
                saveServerPraiseData(viewBinding.editTextPraiseTo.text.toString())
                true
            }
            else -> false
        }
    }

    override fun onClickRecentPraiseToItem(recentPraiseTo: String) {
        viewBinding.editTextPraiseTo.apply {
            setText(recentPraiseTo)
            setSelection(recentPraiseTo.length)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }


    class CustomDialogBuilder {
        private val dialog = HomeDialogDoneFragment()

        fun setPraiseIndex(praiseId: Int): CustomDialogBuilder {
            dialog.praiseId = praiseId
            return this
        }

        fun create(): HomeDialogDoneFragment {
            return dialog
        }
    }
}