package com.example.praisewhale.data

data class ResponseCardBoxData(
    val status: Int,
    val message: String,
    val data: Data
){
    data class Data(
        val collectionPraise: List<CollectionPraise>,
        val nickname: String,
        val praiseCount: Int
    )
}
data class CollectionPraise(
    val praiseName: String,
    val praise: Praise
)
data class Praise(
    val today_praise: String
)