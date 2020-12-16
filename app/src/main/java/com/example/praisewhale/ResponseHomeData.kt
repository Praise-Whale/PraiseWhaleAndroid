package com.example.praisewhale

import com.google.gson.annotations.SerializedName

data class ResponseHomeData(
    val status: Int,
    val message: String,
    val data: Data
) {
    data class Data(
        val id: Int,
        @SerializedName("daily_praise")
        val dailyPraise: String,
        @SerializedName("mission_praise")
        val praiseDescription: String
    )
}