package com.example.praisewhale.api

import com.example.praisewhale.data.RequestCollectionData
import com.example.praisewhale.data.ResponseCardData
import com.example.praisewhale.data.ResponseCollectionData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface RequestInterface {
    //최근 칭찬 유저 조회
    @Headers("Content-Type:application/json")
    @GET("users-praise")
    fun getUsersPraise(

    ) : Call<ResponseCollectionData>

    // 홈 화면 조회
    @GET("/home")
    fun getPraise(

    )  : Call<ResponseCollectionData>

    // 칭찬한 사람 추가
    @Headers("Content-Type:application/json")
    @POST("users/target")
    fun postUsers(
        @Body body : RequestCollectionData
    )  : Call<ResponseCollectionData>

    // 칭찬 컬렉션 조회
    @Headers("Content-Type:application/json")
    @GET("praise/collection")
    fun getCollection(

    ): Call<ResponseCardData>
}