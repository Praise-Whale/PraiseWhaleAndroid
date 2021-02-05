package com.example.praisewhale

import com.example.praisewhale.data.RequestInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object CollectionImpl {
    private const val BASE_URL = "http://52.79.90.119:3000/"
    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service : RequestInterface = retrofit.create(
        RequestInterface::class.java)
}