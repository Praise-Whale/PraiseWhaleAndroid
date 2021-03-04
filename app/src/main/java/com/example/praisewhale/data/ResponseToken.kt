package com.example.praisewhale.data

data class ResponseToken(
    val status: Int,
    val message: String,
    val data: Data
) {
    data class Data(
        val accessToken: String,
        val refreshToken: String
    )
}