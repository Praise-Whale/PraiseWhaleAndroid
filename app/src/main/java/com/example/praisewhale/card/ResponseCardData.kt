package com.example.praisewhale.card.data

data class ResponseCardData(
    val status: Int,
    val message: String,
    val data: Data
){
    data class Data(
        val praiseCount: Int,
        val collectionPraise: List<CollectionPraise>
    )
}
data class CollectionPraise(
    val praiseName: String,
    val created_at: String,
    val today_praise: String
)