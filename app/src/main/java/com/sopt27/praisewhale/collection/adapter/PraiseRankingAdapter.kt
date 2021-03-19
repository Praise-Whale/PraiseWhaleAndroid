package com.sopt27.praisewhale.collection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sopt27.praisewhale.collection.data.ResponsePraiseRanking
import com.sopt27.praisewhale.databinding.ItemPraiseRankingBinding
import com.sopt27.praisewhale.util.PraiseRankingClickListener


class PraiseRankingAdapter(
    private val praiseRankingList: List<ResponsePraiseRanking.Data.RankingResult>,
    private val praiseRankingClickListener: PraiseRankingClickListener
) : RecyclerView.Adapter<PraiseRankingViewHolder>() {

    private var _viewBinding: ItemPraiseRankingBinding? = null
    private val viewBinding get() = _viewBinding!!


    override fun getItemCount(): Int = praiseRankingList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PraiseRankingViewHolder {
        _viewBinding = ItemPraiseRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PraiseRankingViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: PraiseRankingViewHolder, position: Int) {
        praiseRankingList.let {
            holder.onBind(position, it[position])
            holder.itemView.setOnClickListener {
                praiseRankingClickListener.onClickPraiseRankingItem(praiseRankingList[position].praiseTo)
            }
        }
    }
}