package com.example.praisewhale.api

import com.example.praisewhale.RequestCollectionData
import com.example.praisewhale.ResponseCollectionData
import com.example.praisewhale.ResponseUserData
import retrofit2.Call
import retrofit2.http.*

interface RequestInterface {
    //최근 칭찬 유저 조회
    @Headers("Content-Type:application/json")
    @GET("users-praise")
    fun getUsersPraise(

    ) : Call<ResponseCollectionData>

    // 홈 화면 조회
    @Headers("Content-Type:application/json")
    @GET("home")
    fun getPraise(

    )  : Call<ResponseCollectionData>

    // 칭찬한 사람 추가
    @Headers("Content-Type:application/json")
    @POST("users/target")
    fun postUsers(
        @Body body : RequestCollectionData
    )  : Call<ResponseCollectionData>

    // 레벨 조회
    @Headers("Content-Type:application/json")
    @GET("level/praise/{userIdx}")
    fun getuserIdx(
        @Path("userIdx") userIdx : Int
    )  : Call<ResponseUserData>
}