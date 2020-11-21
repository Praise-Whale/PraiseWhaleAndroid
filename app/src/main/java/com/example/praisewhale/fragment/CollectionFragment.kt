package com.example.praisewhale.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.praisewhale.CollectionAdapter
import com.example.praisewhale.R
import com.example.praisewhale.CollectionImpl
import com.example.praisewhale.data.PraiseResult
import com.example.praisewhale.data.ResponseCardData
import com.example.praisewhale.data.ResponseCollectionData
import kotlinx.android.synthetic.main.dialog_positive.*
import kotlinx.android.synthetic.main.fragment_collection.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

        val praiseResult = mutableListOf<PraiseResult>()
        collectionAdapter = CollectionAdapter(view.context)

        rv.adapter = collectionAdapter

        rv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            view.context,
            androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
            false
        )

        val call : Call<ResponseCardData> = CollectionImpl.service.getCollection()
        call.enqueue(object : Callback<ResponseCardData> {
            override fun onFailure(call: Call<ResponseCardData>, t: Throwable) {
                Log.d("tag", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<ResponseCardData>,
                response: Response<ResponseCardData>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let { it ->
                        Log.d("tag", it.toString())
                        tv_total.text = it.data.praiseCount[0].likeCount.toString()
                        // collectionAdapter.data = it.data.praiseResult as MutableList<PraiseResult>
                        collectionAdapter.notifyDataSetChanged()
                    }

            }
        })

        collectionAdapter.data = mutableListOf(
            PraiseResult("11월22일", "넌 참 사랑스러워", PraiseTarget(name="anna")),
            PraiseResult("11월23일", "넌 참 사랑스러워", PraiseTarget(name="gui")),
            PraiseResult("11월24일", "넌 참 사랑스러워", PraiseTarget(name="kmibin"))
        )

        super.onViewCreated(view, savedInstanceState)
    }
}