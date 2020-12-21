package com.example.praisewhale.data.home

import com.google.gson.annotations.SerializedName

data class ResponseDonePraise(
    val status : Int,
    val message : String,
    val data: Data
) {
    data class Data(
        @SerializedName("levelUpCheck")
        val isLevelUp: Boolean,
        val userLevel: Int
    )
}