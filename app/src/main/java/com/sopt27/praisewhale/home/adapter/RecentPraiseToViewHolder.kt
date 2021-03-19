package com.sopt27.praisewhale.home.adapter

import android.content.res.Resources
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.sopt27.praisewhale.databinding.ItemRecentPraiseToBinding


class RecentPraiseToViewHolder(
    private val binding: ItemRecentPraiseToBinding
) : RecyclerView.ViewHolder(binding.root) {


    fun onBind(listSize: Int, position: Int, recentPraiseTo: String) {
        setLastItemMargin(listSize, position)
        binding.textViewRecentPraiseTo.text = recentPraiseTo
    }

    private fun setLastItemMargin(listSize: Int, position: Int) {
        if ((listSize - 1) == position) {
            val layoutParams = ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                (32 * Resources.getSystem().displayMetrics.density).toInt()
            )
            layoutParams.setMargins(0, 0, 0, 0)
            binding.textViewRecentPraiseTo.layoutParams = layoutParams
        }
    }
}