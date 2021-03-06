package com.ade.itemsinmap.datasource.api
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("avito-tech/android-trainee-task/master/pins.json")
    fun getPinsInMap(): Call<PinsInMap>
}
