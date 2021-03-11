package com.example.praisewhale.collection.adapter

import android.view.View
import android.widget.TextView
import com.example.praisewhale.R
import com.example.praisewhale.collection.data.CollectionPraise

class CardBoxViewHolder (itemView : View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){
    private val praisedName : TextView = itemView.findViewById(R.id.tv_card_name)
    private val createdAt : TextView = itemView.findViewById(R.id.tv_card_date)
    private val todayPraise : TextView = itemView.findViewById(R.id.tv_card_praise)

    fun onBind(data: CollectionPraise){
        praisedName.text = data.praisedName
        createdAt.text = data.created_at.substring(5,7).toInt().toString()+"월 "+data.created_at.substring(8,10).toInt().toString()+"일"
        todayPraise.text = data.today_praise
    }
}