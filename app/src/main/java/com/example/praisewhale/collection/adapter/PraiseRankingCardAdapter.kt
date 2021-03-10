package com.example.praisewhale.collection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.praisewhale.collection.data.ResponsePraiseRankingCard
import com.example.praisewhale.databinding.ItemCardBoxCardBinding


class PraiseRankingCardAdapter(
    private val praiseRankingList: List<ResponsePraiseRankingCard.Data.PraiseCollection>
) : RecyclerView.Adapter<PraiseRankingCardViewHolder>() {

    private var _viewBinding: ItemCardBoxCardBinding? = null
    private val viewBinding get() = _viewBinding!!


    override fun getItemCount(): Int = praiseRankingList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PraiseRankingCardViewHolder {
        _viewBinding = ItemCardBoxCardBinding.inflate(LayoutInflater.from(parent.context))
        return PraiseRankingCardViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: PraiseRankingCardViewHolder, position: Int) {
        holder.onBind(praiseRankingList[position])
    }
}