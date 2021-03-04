package com.example.praisewhale.collection.data

import com.google.gson.annotations.SerializedName

data class ResponsePraiseRankingCard(
    val status: Int,
    val message: String,
    val data: Data
) {
    data class Data(
        val praiseCount: Int,
        @SerializedName("collectionPraise")
        val praiseCollection: List<PraiseCollection>
    ) {
        data class PraiseCollection(
            @SerializedName("praisedName")
            val praisedTo: String,
            val createdAt: String,
            @SerializedName("todayPraise")
            val praise: String
        )
    }
}