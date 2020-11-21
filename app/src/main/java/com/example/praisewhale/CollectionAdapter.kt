package com.example.praisewhale

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CollectionAdapter  (private val context : Context) : RecyclerView.Adapter<CollectionViewHolder>(){
    var data = mutableListOf<CollectionData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.collection_item,
            parent, false)

        return CollectionViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.onBind(data[position])
    }
}