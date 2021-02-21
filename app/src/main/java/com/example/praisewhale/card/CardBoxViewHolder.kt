package com.example.praisewhale.card

import android.view.View
import android.widget.TextView
import com.example.praisewhale.R
import com.example.praisewhale.card.data.CollectionPraise

class CardBoxViewHolder (itemView : View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){
    private val praiseName : TextView = itemView.findViewById(R.id.tv_card_name)
    private val createdAt : TextView = itemView.findViewById(R.id.tv_card_date)
    private val todayPraise : TextView = itemView.findViewById(R.id.tv_card_praise)

    fun onBind(data: CollectionPraise){
        praiseName.text = ellipsis(data.praiseName, 5, "...")
        createdAt.text = data.created_at.substring(5,6)+"월"+data.created_at.substring(8,9)+"일"
        todayPraise.text = data.today_praise
    }

    // 최대 길이를 초과할 경우 말줄임 처리
    private fun ellipsis(str: String?, maxLength: Int, ellipsis: String): String {
        val length: Int = str!!.length

        if (length <= maxLength) { return str }

        return str.substring(0, maxLength) + ellipsis
    }
}