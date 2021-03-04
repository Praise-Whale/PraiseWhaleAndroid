package com.example.praisewhale.collection.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.praisewhale.R
import com.example.praisewhale.collection.data.ResponsePraiseRanking
import com.example.praisewhale.databinding.ItemPraiseRankingBinding


class PraiseRankingViewHolder(
    private val viewBinding: ItemPraiseRankingBinding
) : RecyclerView.ViewHolder(viewBinding.root) {


    fun onBind(position: Int, rankingData: ResponsePraiseRanking.Data.RankingResult) {
        setRankingInfoImageResource(position)
        viewBinding.apply {
            textViewPraiseTo.text = rankingData.praiseTo
            textViewPraiseCount.text = "${rankingData.praiseCount}번"
        }
    }

    private fun setRankingInfoImageResource(position: Int) {
        viewBinding.imageViewRankingInfo.apply {
            when (position) {
                0 -> setImageResource(R.drawable.ranking_circle_number_1)
                1 -> setImageResource(R.drawable.ranking_circle_number_2)
                2 -> setImageResource(R.drawable.ranking_circle_number_3)
                3 -> setImageResource(R.drawable.ranking_circle_number_4)
                4 -> setImageResource(R.drawable.ranking_circle_number_5)
            }
        }
    }
}