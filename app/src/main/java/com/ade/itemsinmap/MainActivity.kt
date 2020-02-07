package com.ade.itemsinmap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ade.itemsinmap.datasource.api.ApiService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


open class MainActivity : AppCompatActivity()

fun createApiService(): ApiService {
    val gson = GsonBuilder().create()
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    return retrofit.create(ApiService::class.java)

}


