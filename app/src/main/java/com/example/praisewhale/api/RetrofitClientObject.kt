package com.example.praisewhale.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientObject {

    private var instance: Retrofit? = null
    private val gson = GsonBuilder().setLenient().create()


    // 서버 주소
    private const val BASE_URL = "http://52.79.90.119:3000/"

    // SingleTon
    fun getInstance(): Retrofit {
        if (instance == null) {
            instance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return instance!!
    }
}