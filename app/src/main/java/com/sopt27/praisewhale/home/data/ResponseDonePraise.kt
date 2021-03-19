package com.sopt27.praisewhale.home.data

import com.google.gson.annotations.SerializedName


data class ResponseDonePraise(
    val status : Int,
    val message : String,
    val data: Data
) {
    data class Data(
        @SerializedName("levelCheck")
        val isLevelUp: Boolean,
        val userLevel: Int
    )
}