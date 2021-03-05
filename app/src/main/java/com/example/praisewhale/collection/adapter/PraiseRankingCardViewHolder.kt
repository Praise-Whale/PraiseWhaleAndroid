package com.example.praisewhale.collection.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.praisewhale.collection.data.ResponsePraiseRankingCard
import com.example.praisewhale.databinding.ItemCardBoxCardBinding
import java.text.SimpleDateFormat
import java.util.*


class PraiseRankingCardViewHolder(
    private val viewBinding: ItemCardBoxCardBinding
) : RecyclerView.ViewHolder(viewBinding.root) {


    fun onBind(rankingData: ResponsePraiseRankingCard.Data.PraiseCollection) {
        viewBinding.apply {
            tvCardName.text = rankingData.name
            tvCardPraise.text = rankingData.praise
            tvCardDate.text = getFormattedDate(rankingData.date)
        }
    }

    private fun getFormattedDate(date: String): String {
        val pattern = "yyyy-MM-dd'T'HH:mm:ss.sss'Z'"
        val dateFormat = SimpleDateFormat(pattern, Locale.KOREAN).parse(date)!!
        return SimpleDateFormat("M'월' d'일'", Locale.KOREA).format(dateFormat)
    }
}