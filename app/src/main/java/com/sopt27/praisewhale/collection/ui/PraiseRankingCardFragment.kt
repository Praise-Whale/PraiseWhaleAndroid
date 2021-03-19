package com.sopt27.praisewhale.collection.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearSnapHelper
import com.sopt27.praisewhale.CollectionImpl
import com.sopt27.praisewhale.collection.adapter.PraiseRankingCardAdapter
import com.sopt27.praisewhale.collection.data.ResponsePraiseRankingCard
import com.sopt27.praisewhale.collection.ui.CollectionTabFragment.Companion.SWITCH_HEIGHT
import com.sopt27.praisewhale.collection.ui.PraiseRankingFragment.Companion.PRAISE_TARGET
import com.sopt27.praisewhale.data.ResponseToken
import com.sopt27.praisewhale.databinding.FragmentPraiseRankingCardBinding
import com.sopt27.praisewhale.util.MyApplication
import com.sopt27.praisewhale.util.fadeOut
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PraiseRankingCardFragment : Fragment() {

    private var _viewBinding: FragmentPraiseRankingCardBinding? = null
    private val viewBinding get() = _viewBinding!!
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private val sharedPreferences = MyApplication.mySharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentPraiseRankingCardBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setButtonPraiseTargetHeight()
        setOnBackPressedCallBack()
    }

    override fun onResume() {
        super.onResume()
        setUserInfo()
        getPraiseRankingCardData()
        if (!onBackPressedCallback.isEnabled) {
            onBackPressedCallback.isEnabled = true
        }
    }

    override fun onPause() {
        super.onPause()
        onBackPressedCallback.isEnabled = false
    }

    private fun setUserInfo() {
        viewBinding.apply {
            textViewUserName.text = "${sharedPreferences.getValue("nickName", "")}님의"
            buttonPraiseTarget.text = PRAISE_TARGET
        }
    }

    private fun setListeners() {
        viewBinding.imageButtonBack.setOnClickListener(fragmentOnClickListener)
    }

    private fun getPraiseRankingCardData() {
        val call: Call<ResponsePraiseRankingCard> = CollectionImpl.service.getPraiseRankingCard(
            sharedPreferences.getValue("token", ""),
            PRAISE_TARGET
        )
        call.enqueue(object : Callback<ResponsePraiseRankingCard> {
            override fun onFailure(call: Call<ResponsePraiseRankingCard>, t: Throwable) {
                Log.d("tag", t.localizedMessage!!)
            }

            override fun onResponse(
                call: Call<ResponsePraiseRankingCard>,
                response: Response<ResponsePraiseRankingCard>
            ) {
                when (response.isSuccessful) {
                    true -> setPraiseRankingCardView(response.body()!!.data)
                    false -> handlePraiseRankingCardStatusCode(response)
                }
            }
        })
    }

    private fun setPraiseRankingCardView(praiseRankingCardData: ResponsePraiseRankingCard.Data) {
        viewBinding.textViewPraiseRankingCardTitle01.text = "${praiseRankingCardData.praiseCount}번"
        setRecyclerView(praiseRankingCardData.praiseCollection)
        fadeOutLoadingView()
    }

    private fun setRecyclerView(praiseRankingCardList: List<ResponsePraiseRankingCard.Data.PraiseCollection>) {
        viewBinding.recyclerViewPraiseRankingCard.apply {
            adapter = PraiseRankingCardAdapter(praiseRankingCardList)
            if (onFlingListener == null) {
                LinearSnapHelper().attachToRecyclerView(this)
            }
        }
    }

    private fun fadeOutLoadingView() {
        viewBinding.viewLoading.fadeOut()
    }

    private fun handlePraiseRankingCardStatusCode(response: Response<ResponsePraiseRankingCard>) {
        when (response.code()) {
            400 -> updateToken()
            else -> {
                Log.d(
                    "TAG",
                    "PraiseRankingCardFragment - handlePraiseDataStatusCode: ${response.code()}"
                )
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
                        getPraiseRankingCardData()
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

    private fun setButtonPraiseTargetHeight() {
        viewBinding.buttonPraiseTarget.height = SWITCH_HEIGHT
    }

    private fun setOnBackPressedCallBack() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (parentFragment as CollectionFragment).showFragmentCollectionTab()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    private val fragmentOnClickListener = View.OnClickListener {
        viewBinding.apply {
            when (it.id) {
                imageButtonBack.id -> (parentFragment as CollectionFragment).showFragmentCollectionTab()
            }
        }
    }
}