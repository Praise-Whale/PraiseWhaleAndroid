package com.example.praisewhale.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.praisewhale.CollectionAdapter
import com.example.praisewhale.CollectionData
import com.example.praisewhale.R
import kotlinx.android.synthetic.main.fragment_collection.*


class CollectionFragment : Fragment() {

    private lateinit var collectionAdapter: CollectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_collection, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        collectionAdapter = CollectionAdapter(view.context)

        rv.adapter = collectionAdapter
        rv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            view.context,
            androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
            false
        )

        collectionAdapter.data = mutableListOf(
            CollectionData("11월 20일", "1", "message1", "1"),
            CollectionData("11월 20일", "2", "message2", "2"),
            CollectionData("11월 20일", "3", "message3", "3")
        )

        super.onViewCreated(view, savedInstanceState)
    }
}