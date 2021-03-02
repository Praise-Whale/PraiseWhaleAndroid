package com.example.praisewhale.collection.data

import com.google.gson.annotations.SerializedName

data class ResponsePraiseRanking(
    val status: Int,
    val message: String,
    val data: Data
) {
    data class Data(
        @SerializedName("totalPraiserCount")
        val totalPraiseToCount: Int,
        @SerializedName("rankingCountResult")
        val rankingResult: List<RankingResult>
    ) {
        data class RankingResult(
            @SerializedName("praisedName")
            val praiseTo: String,
            @SerializedName("praiserCount")
            val praiseCount: Int
        )
    }
}