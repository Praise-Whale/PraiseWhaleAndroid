package com.example.praisewhale

import android.view.View
import android.widget.TextView

class CollectionViewHolder (itemView : View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){
    private val count : TextView = itemView.findViewById(R.id.tv_count)
    private val name : TextView = itemView.findViewById(R.id.tv_name)
    private val message : TextView = itemView.findViewById(R.id.tv_msg)

    fun onBind(data : CollectionData){
        count.text = data.count
        name.text = data.name
        message.text = data.message
    }
}