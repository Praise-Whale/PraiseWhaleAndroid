package com.example.praisewhale.home.data

import com.google.gson.annotations.SerializedName

data class ResponseHomePraise(
    val status: Int,
    val message: String,
    val data: Data
) {
    data class Data(
        val praiseId: Int,
        @SerializedName("today_praise")
        val dailyPraise: String,
        @SerializedName("praise_description")
        val praiseDescription: String
    )
}