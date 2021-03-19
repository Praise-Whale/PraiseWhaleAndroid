package com.sopt27.praisewhale.collection.data

data class ResponseCardData(
    val status: Int,
    val message: String,
    val data: Data
){
    data class Data(
        val praiseCount: Int,
        val firstDate: FirstDate,
        val collectionPraise: List<CollectionPraise>
    )
}
data class CollectionPraise(
    val praisedName: String,
    val created_at: String,
    val today_praise: String
)
data class FirstDate(
    val created_at: String
)