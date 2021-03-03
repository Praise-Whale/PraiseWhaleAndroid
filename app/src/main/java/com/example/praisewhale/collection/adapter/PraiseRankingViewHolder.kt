package com.example.praisewhale.collection.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.praisewhale.R
import com.example.praisewhale.collection.data.ResponsePraiseRanking
import com.example.praisewhale.databinding.ItemPraiseRankingBinding
import com.example.praisewhale.util.setContextCompatBackgroundTintList


class PraiseRankingViewHolder(
    private val viewBinding: ItemPraiseRankingBinding
) : RecyclerView.ViewHolder(viewBinding.root) {


    fun onBind(position: Int, rankingData: ResponsePraiseRanking.Data.RankingResult) {
        setMatchParentToRecyclerView()
        setRankingInfoBackgroundColor(position)
        viewBinding.apply {
            buttonRankingInfo.text = (position + 1).toString()
            textViewPraiseTo.text = rankingData.praiseTo
            textViewPraiseCount.text = "${rankingData.praiseCount}번"
        }
    }

    private fun setMatchParentToRecyclerView() {
        val layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        viewBinding.root.layoutParams = layoutParams
    }

    private fun setRankingInfoBackgroundColor(position: Int) {
        viewBinding.apply {
            when (position) {
                0 -> buttonRankingInfo.setContextCompatBackgroundTintList(R.color.sand_yellow)
                1, 2 -> buttonRankingInfo.setContextCompatBackgroundTintList(R.color.pale)
                else -> buttonRankingInfo.setContextCompatBackgroundTintList(R.color.grey_4)
            }
        }
    }
}