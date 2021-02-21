package com.example.praisewhale.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.praisewhale.databinding.ItemRecentPraiseToBinding
import com.example.praisewhale.home.data.ResponseRecentPraiseTo
import com.example.praisewhale.util.RecentPraiseToClickListener


class RecentPraiseToAdapter(
    private val recentPraiseToList: List<ResponseRecentPraiseTo.Name>,
    private val recentPraiseToClickListener: RecentPraiseToClickListener
) : RecyclerView.Adapter<RecentPraiseToViewHolder>() {

    private var _binding: ItemRecentPraiseToBinding? = null
    private val binding get() = _binding!!


    override fun getItemCount(): Int = recentPraiseToList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentPraiseToViewHolder {
        _binding = ItemRecentPraiseToBinding.inflate(LayoutInflater.from(parent.context))
        return RecentPraiseToViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentPraiseToViewHolder, position: Int) {
        holder.onBind(recentPraiseToList.size, position, recentPraiseToList[position].name)
        holder.itemView.setOnClickListener {
            recentPraiseToClickListener.onClickRecentPraiseToItem(recentPraiseToList[position].name)
        }
    }
}