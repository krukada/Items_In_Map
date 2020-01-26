package com.ade.itemsinmap

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface ApiService {
    @GET("avito-tech/android-trainee-task/master/pins.json")
    fun load(): Call<PinsInMap>

    companion object Factory{
        fun create(): ApiService{
            val gson = GsonBuilder().create()
            val retrofit:Retrofit = Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            return retrofit.create(ApiService::class.java)

        }
    }
}