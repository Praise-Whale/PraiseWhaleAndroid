package com.example.praisewhale.data

class ResponseCardData (
    val status : Int,
    val message : String,
    val data : CardData
) {
    data class CardData (
        val praiseResult : List<PraiseResult>,
        val praiseCount : List<PraiseCount>
    )
}
data class PraiseResult (
    val createAt : String,
    val daily_praise : String,
    val praiseTarget : PraiseTarget
)
data class PraiseTarget (
    val name : String
)
data class PraiseCount (
    val likeCount : Int
)