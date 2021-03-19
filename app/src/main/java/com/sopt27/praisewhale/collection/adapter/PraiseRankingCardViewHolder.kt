package com.sopt27.praisewhale.collection.adapter

import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.sopt27.praisewhale.collection.data.ResponsePraiseRankingCard
import com.sopt27.praisewhale.databinding.ItemCardBoxCardBinding
import java.text.SimpleDateFormat
import java.util.*


class PraiseRankingCardViewHolder(
    private val viewBinding: ItemCardBoxCardBinding
) : RecyclerView.ViewHolder(viewBinding.root) {


    fun onBind(rankingData: ResponsePraiseRankingCard.Data.PraiseCollection) {
        setMatchParentToRecyclerView()
        viewBinding.apply {
            tvCardName.text = rankingData.name
            tvCardPraise.text = rankingData.praise
            tvCardDate.text = getFormattedDate(rankingData.date)
        }
    }

    private fun setMatchParentToRecyclerView() {
        val layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        viewBinding.root.layoutParams = layoutParams
    }

    private fun getFormattedDate(date: String): String {
        val pattern = "yyyy-MM-dd'T'HH:mm:ss.sss'Z'"
        val dateFormat = SimpleDateFormat(pattern, Locale.KOREAN).parse(date)!!
        return SimpleDateFormat("M'월' d'일'", Locale.KOREA).format(dateFormat)
    }
}