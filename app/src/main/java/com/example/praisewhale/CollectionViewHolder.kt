package com.example.praisewhale

import android.view.View
import android.widget.TextView
import com.example.praisewhale.data.PraiseResult

class CollectionViewHolder (itemView : View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){

    private val date : TextView = itemView.findViewById(R.id.tv_date)
    private val name : TextView = itemView.findViewById(R.id.tv_name)
    private val message : TextView = itemView.findViewById(R.id.tv_msg)

    fun onBind(data: PraiseResult){
        date.text = data.createAt
        name.text = data.praiseTarget.name
        message.text = data.daily_praise
    }
}