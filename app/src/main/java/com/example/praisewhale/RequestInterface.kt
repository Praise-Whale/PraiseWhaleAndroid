package com.example.praisewhale

import com.example.praisewhale.data.ResponseCardData
import com.example.praisewhale.data.ResponseCollectionData
import retrofit2.Call
import retrofit2.http.*

interface RequestInterface {
    //최근 칭찬 유저 조회
    @Headers("Content-Type:application/json")
    @GET("users-praise")
    fun getUsersPraise(

    ): Call<ResponseCollectionData>

    // 홈 화면 조회
    @Headers("Content-Type:application/json")
    @GET("home")
    fun getPraise(
    ): Call<ResponseHomeData>

    // 칭찬한 사람 추가
    @Headers("Content-Type:application/json")
    @POST("users/target")
    fun postUsers(
        @Body body: ResponsePraiseTargetData
    ): Call<ResponsePraiseTargetData>

    // 칭찬 컬렉션 조회
    @Headers("Content-Type:application/json")
    @GET("praise/collection")
    fun getCollection(

    ): Call<ResponseCardData>

    @Headers("Content-Type:application/json")
    @GET("level/praise/{userIdx}")
    fun getuserIdx(
        @Path("userIdx") userIdx : Int
    )  : Call<ResponseUserData>
}