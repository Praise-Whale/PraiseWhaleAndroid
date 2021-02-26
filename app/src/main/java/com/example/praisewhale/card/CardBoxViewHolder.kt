package com.example.praisewhale.card

import android.view.View
import android.widget.TextView
import com.example.praisewhale.R

class CardBoxViewHolder (itemView : View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){
    private val praisedName : TextView = itemView.findViewById(R.id.tv_card_name)
    private val createdAt : TextView = itemView.findViewById(R.id.tv_card_date)
    private val todayPraise : TextView = itemView.findViewById(R.id.tv_card_praise)

    fun onBind(data: CollectionPraise){
        praisedName.text = ellipsis(data.praisedName, 5, "...")
        createdAt.text = data.created_at.substring(5,7).toInt().toString()+"월 "+data.created_at.substring(8,10).toInt().toString()+"일"
        todayPraise.text = data.today_praise
    }

    // 최대 길이를 초과할 경우 말줄임 처리
    private fun ellipsis(str: String, maxLength: Int, ellipsis: String): String {
        val length: Int = str.length

        if (length <= maxLength) { return str }

        return str.substring(0, maxLength) + ellipsis
    }
}