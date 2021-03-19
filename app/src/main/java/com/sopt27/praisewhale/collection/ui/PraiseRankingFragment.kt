package com.sopt27.praisewhale.collection.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sopt27.praisewhale.CollectionImpl
import com.sopt27.praisewhale.collection.adapter.PraiseRankingAdapter
import com.sopt27.praisewhale.collection.data.ResponsePraiseRanking
import com.sopt27.praisewhale.data.ResponseToken
import com.sopt27.praisewhale.databinding.FragmentPraiseRankingBinding
import com.sopt27.praisewhale.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PraiseRankingFragment : Fragment(), PraiseRankingClickListener {

    private var _viewBinding: FragmentPraiseRankingBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val sharedPreferences = MyApplication.mySharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentPraiseRankingBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onResume() {
        super.onResume()
        getServerPraiseRankingData()
    }

    private fun getServerPraiseRankingData() {
        val call: Call<ResponsePraiseRanking> = CollectionImpl.service.getPraiseRanking(
            sharedPreferences.getValue("token", "")
        )
        call.enqueue(object : Callback<ResponsePraiseRanking> {
            override fun onFailure(call: Call<ResponsePraiseRanking>, t: Throwable) {
                Log.d("tag", t.localizedMessage!!)
            }

            override fun onResponse(
                call: Call<ResponsePraiseRanking>,
                response: Response<ResponsePraiseRanking>
            ) {
                when (response.isSuccessful) {
                    true -> setView(response.body()!!.data)
                    false -> handlePraiseRankingStatusCode(response)
                }
            }
        })
    }

    private fun setView(praiseRankingData: ResponsePraiseRanking.Data) {
        when (praiseRankingData.rankingResult.size) {
            0 -> setEmptyViewVisibility()
            else -> {
                setRankingViewVisibility()
                setRankingView(praiseRankingData)
            }
        }
        fadeOutLoadingView()
    }

    private fun handlePraiseRankingStatusCode(response: Response<ResponsePraiseRanking>) {
        when (response.code()) {
            400 -> updateToken()
            else -> {
                Log.d("TAG", "PraiseRankingFragment - handlePraiseDataStatusCode: ${response.code()}")
                return
            }
        }
    }

    private fun setEmptyViewVisibility() {
        viewBinding.apply {
            textViewRankingTitle01.visibility = View.INVISIBLE
            textViewRankingTitle02.visibility = View.INVISIBLE
            recyclerViewPraiseRanking.visibility = View.INVISIBLE

            imageViewEmpty.visibility = View.VISIBLE
            textViewEmptyTitle.visibility = View.VISIBLE
            textViewEmptyDescription.visibility = View.VISIBLE
        }
    }

    private fun setRankingViewVisibility() {
        viewBinding.apply {
            textViewRankingTitle01.visibility = View.VISIBLE
            textViewRankingTitle02.visibility = View.VISIBLE
            recyclerViewPraiseRanking.visibility = View.VISIBLE

            imageViewEmpty.visibility = View.INVISIBLE
            textViewEmptyTitle.visibility = View.INVISIBLE
            textViewEmptyDescription.visibility = View.INVISIBLE
        }
    }

    private fun setRankingView(praiseRankingData: ResponsePraiseRanking.Data) {
        setRankingRecyclerView(praiseRankingData)
        viewBinding.textViewRankingTitle01.text = "${praiseRankingData.totalPraiseToCount}명"
    }

    private fun setRankingRecyclerView(praiseRankingData: ResponsePraiseRanking.Data) {
        viewBinding.recyclerViewPraiseRanking.apply {
            addOnScrollListener(scrollListener)
            adapter = PraiseRankingAdapter(praiseRankingData.rankingResult, this@PraiseRankingFragment)
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
                        getServerPraiseRankingData()
                    }
                    false -> {
                        Log.d("TAG", "PraiseRankingFragment - onResponse: error")
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

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            (recyclerView.layoutManager as LinearLayoutManager).apply {
                updateBlurBox(findFirstCompletelyVisibleItemPosition())
            }
        }
    }

    private fun updateBlurBox(firstCompletelyVisibleItemPosition: Int) {
        viewBinding.imageViewBlurBoxTop.apply {
            when (firstCompletelyVisibleItemPosition) {
                0 -> fadeOut()
                else -> fadeIn()
            }
        }
    }

    private fun fadeOutLoadingView() {
        viewBinding.viewLoading.fadeOut()
    }

    override fun onClickPraiseRankingItem(praiseTo: String) {
        (parentFragment as CollectionTabFragment).showFragmentPraiseRankingCard()
        PRAISE_TARGET = praiseTo
    }


    companion object {
        var PRAISE_TARGET = ""
    }
}