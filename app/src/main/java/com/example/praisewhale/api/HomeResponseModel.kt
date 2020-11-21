package com.example.praisewhale.api

import com.google.gson.annotations.SerializedName

data class HomeResponseModel(
    val status: Int,
    val message: String
) {
    data class Data(
        val id: Int,
        @SerializedName("daily_praise")
        val dailyPraise: String,
        @SerializedName("mission_praise")
        val missionPraise: String
    )
}