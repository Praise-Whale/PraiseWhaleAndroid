package com.example.praisewhale.data

import com.example.praisewhale.ResponseUserData
import com.example.praisewhale.ResponselevelData
import com.example.praisewhale.data.home.ResponseHomePraise
import com.example.praisewhale.data.home.ResponseDonePraise
import com.example.praisewhale.data.home.ResponseRecentPraiseTo
import retrofit2.Call
import retrofit2.http.*

interface RequestInterface {
    // 홈 화면 조회
    @Headers("Content-Type:application/json")
    @GET("home")
    fun getPraise(
        @Header("token") token : String
    ): Call<ResponseHomePraise>

    // 최근 칭찬 유저 조회
    @Headers("Content-Type:application/json")
    @GET("praise/target")
    fun getRecentPraiseTo(
        @Header("token") token : String
    ): Call<ResponseRecentPraiseTo>

    // 칭찬한 사람 추가
    @Headers("Content-Type:application/json")
    @POST("praise/{praiseId}")
    fun postPraiseDone(
        @Header("token") token : String,
        @Path("praiseId") praiseId : String,
        @Body praisedName: String
    ): Call<ResponseDonePraise>

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

    // 닉네임중복체크
    @GET("users/check/{nickname}")
    fun nicknameCheck(
        @Path("nickname") nickname : String
    ) : Call<ResponseData>

    // 회원가입
    @POST("users/signup")
    fun signUp(
        @Body body : RequestSignUp
    ) : Call<ResponseToken>

    // 로그인
    @POST("users/signin")
    fun signIn(
        @Body body : RequestSignIn
    ) : Call<ResponseToken>

    @Headers("Content-Type:application/json")
    @GET("users/home")
    fun getlevelcount(
        @Header("token") token : String

    ) : Call<ResponselevelData>
}