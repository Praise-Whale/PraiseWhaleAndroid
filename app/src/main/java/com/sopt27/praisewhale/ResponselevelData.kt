package com.sopt27.praisewhale

data class ResponselevelData (
    val status : Int,
    val message : String,
    val data : Data
){
    data class Data(
        val nickName : String,
        val whaleName : String,
        val userLevel : Int,
        val praiseCount : Int,
        val levelUpNeedCount : Int
    )

}