package com.sopt27.praisewhale

import com.sopt27.praisewhale.data.RequestInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object CollectionImpl {
    private const val BASE_URL = "http://52.78.101.245/"
    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service : RequestInterface = retrofit.create(
        RequestInterface::class.java)
}