package com.example.praisewhale

data class ResponseUserData(
    val status : Int,
    val message : String,
    val data : Data
) {
    data class Data(
        val userLevel: Int,
        val needLikeCount: Int

    )
}
