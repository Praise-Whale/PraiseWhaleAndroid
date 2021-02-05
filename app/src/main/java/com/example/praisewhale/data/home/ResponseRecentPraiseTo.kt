package com.example.praisewhale.data.home

import com.google.gson.annotations.SerializedName


data class ResponseRecentPraiseTo(
    val status: Int,
    val message: String,
    val data: List<Name>
) {
    data class Name(
        @SerializedName("praisedName")
        val name: String
    )
}