package com.example.praisewhale.api


data class RecentUserResponseModel(
    val status: Int,
    val message: String,
    val data: List<String>
)