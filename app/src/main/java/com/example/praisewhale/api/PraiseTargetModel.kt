package com.example.praisewhale.api


data class PraiseTargetModel(
    val status: Int,
    val message: String,
    val data: List<String>
)