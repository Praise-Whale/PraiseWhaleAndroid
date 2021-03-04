package com.example.praisewhale.collection.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.praisewhale.R
import com.example.praisewhale.collection.data.ResponsePraiseRanking
import com.example.praisewhale.databinding.ItemPraiseRankingBinding
import com.example.praisewhale.util.setContextCompatBackgroundTintList


class PraiseRankingViewHolder(
    private val viewBinding: ItemPraiseRankingBinding
) : RecyclerView.ViewHolder(viewBinding.root) {


    fun onBind(position: Int, rankingData: ResponsePraiseRanking.Data.RankingResult) {
        setRankingInfoBackgroundColor(position)
        viewBinding.apply {
            buttonRankingInfo.text = (position + 1).toString()
            textViewPraiseTo.text = rankingData.praiseTo
            textViewPraiseCount.text = "${rankingData.praiseCount}번"
        }
    }

    private fun setRankingInfoBackgroundColor(position: Int) {
        viewBinding.buttonRankingInfo.apply {
            when (position) {
                0 -> setContextCompatBackgroundTintList(R.color.sand_yellow)
                1, 2 -> setContextCompatBackgroundTintList(R.color.pale)
                else -> setContextCompatBackgroundTintList(R.color.grey_4)
            }
        }
    }
}