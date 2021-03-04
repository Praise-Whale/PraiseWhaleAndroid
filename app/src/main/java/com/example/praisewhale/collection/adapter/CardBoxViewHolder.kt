package com.example.praisewhale.collection.adapter

import android.content.res.Resources
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.praisewhale.R
import com.example.praisewhale.collection.data.CollectionPraise

class CardBoxViewHolder (itemView : View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){
    private val praisedName : TextView = itemView.findViewById(R.id.tv_card_name)
    private val createdAt : TextView = itemView.findViewById(R.id.tv_card_date)
    private val todayPraise : TextView = itemView.findViewById(R.id.tv_card_praise)

    fun onBind(position: Int, data: CollectionPraise){
        setFirstItemMargin(position)
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

    private fun setFirstItemMargin(position: Int) {
        if (position == 0) {
            val layoutParams = ConstraintLayout.LayoutParams(0, 0)
            layoutParams.setMargins((32 * Resources.getSystem().displayMetrics.density).toInt(), 0, 0, 0)
            itemView.rootView.layoutParams = layoutParams
        }
    }
}