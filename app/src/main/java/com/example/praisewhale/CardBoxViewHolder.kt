package com.example.praisewhale

import android.view.View
import android.widget.TextView
import java.lang.StringBuilder

class CardBoxViewHolder (itemView : View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){
    private val praiseName : TextView = itemView.findViewById(R.id.tv_card_name)
    private val date : TextView = itemView.findViewById(R.id.tv_card_date)
    private val praise : TextView = itemView.findViewById(R.id.tv_card_praise)

    fun onBind(data: CardBoxData){
        praiseName.text = ellipsis(data.praiseName.toString(), 5, "...")
        date.text = data.date
        praise.text = data.praise
    }

    // 최대 길이를 초과할 경우 말줄임 처리
    private fun ellipsis(str: String, maxLength: Int, ellipsis: String): String {
        val length: Int = str.length

        if (length <= maxLength) { return str }

        return str.substring(0, maxLength) + ellipsis
    }
}