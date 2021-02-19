package com.example.praisewhale.data

import com.example.praisewhale.ResponseUserData
import com.example.praisewhale.ResponselevelData


import com.example.praisewhale.data.home.ResponseNickChange
import com.example.praisewhale.home.data.RequestPraiseDone
import com.example.praisewhale.home.data.ResponseDonePraise
import com.example.praisewhale.home.data.ResponseHomePraise
import com.example.praisewhale.home.data.ResponseRecentPraiseTo


import retrofit2.Call
import retrofit2.http.*

interface RequestInterface {

    // HomeFragment.kt - 홈 화면 조회
    @Headers("Content-Type:application/json")
    @GET("home")
    fun getPraise(
        @Header("token") token: String
    ): Call<ResponseHomePraise>

    // HomeDialogDoneFragment.kt - 최근 칭찬 유저 조회
    @Headers("Content-Type:application/json")
    @GET("praise/target")
    fun getRecentPraiseTo(
        @Header("token") token: String
    ): Call<ResponseRecentPraiseTo>

    // HomeDialogDoneFragment.kt - 칭찬한 사람 추가
    @Headers("Content-Type:application/json")
    @POST("praise/{praiseId}")
    fun postPraiseDone(
        @Header("token") token: String,
        @Path("praiseId") praiseId: Int,
        @Body body: RequestPraiseDone
    ): Call<ResponseDonePraise>


    // 칭찬 컬렉션 전체 조회
    @Headers("Content-Type:application/json")
    @GET("praise/collection")
    fun getCollection(

    ): Call<ResponseCardBoxData>

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

    // 닉네임 변경
    @Headers("Content-Type:application/json")
    @PUT("users/nickname")
    fun nickchange(
        @Header("token") token : String,
        @Body body :RequestNickChange
    ) : Call<ResponseNickChange>

}