package com.example.praisewhale.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RetrofitService {

    @Headers("Content-Type:application/json")
    @GET("home")
    fun getHomeRequest(
        @Query("status") id: Int,
        @Query("message") dailyPraise: String,
        @Query("message") missionPraise: String
    ): Call<HomeResponseModel.Data>

    @GET("users-praise")
    fun getRecentUsersPraise(
        @Query("status") status: Int,
        @Query("message") message: String,
        @Query("data") data: List<String>
    ): Call<RecentUserResponseModel>

    @GET("users/target")
    fun addPraiseTarget(
        @Query("id") id: Int,
        @Query("name") name: String
    ): Call<PraiseTargetModel>

}