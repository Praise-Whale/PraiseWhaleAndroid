package com.example.praisewhale.collection.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.praisewhale.collection.data.ResponsePraiseRanking
import com.example.praisewhale.collection.data.ResponsePraiseRankingCard
import com.example.praisewhale.databinding.ItemCardBoxCardBinding


class PraiseRankingCardViewHolder(
    private val viewBinding: ItemCardBoxCardBinding
) : RecyclerView.ViewHolder(viewBinding.root) {


    fun onBind(position: Int, rankingData: ResponsePraiseRankingCard.Data.PraiseCollection) {
    }
}