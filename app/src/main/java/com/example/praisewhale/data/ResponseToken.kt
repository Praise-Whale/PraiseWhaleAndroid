package com.example.praisewhale.data

data class ResponseToken (
    val status : Int,
    val message : String,
    val data : Token
)

data class Token (
    val accessToken : String,
    val refreshToken : String
)