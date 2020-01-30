package com.ade.itemsinmap

import android.util.Log
import com.google.gson.GsonBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PinsRepository {

    fun createApiService(): ApiService{
        val gson = GsonBuilder().create()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(ApiService::class.java)

    }
    fun getPinsInMap(db: AppDatabase){

        val apiService:ApiService = createApiService()

        apiService.getPinsInMap().enqueue(object : Callback<PinsInMap> {
            override fun onResponse(call: Call<PinsInMap>, response: Response<PinsInMap>) {
                if (response.isSuccessful) {
                    GlobalScope.launch {
                        if (db.userDao().getAll().isEmpty()) {
                            addAllInDataBase(response,db)
                        } else {
                            addPinInDataBase(response,db)
                        }
                        println(db.userDao().getAll().size)
                    }

                    Log.i( R.string.success.toString() ," "+ response.code())
                } else {
                    Log.e(R.string.error.toString(), " "+ response.code())
                }
            }
            override fun onFailure(call: Call<PinsInMap>, t: Throwable) {
                t.printStackTrace()

            }

        })

    }
    fun addAllInDataBase(response: Response<PinsInMap>,db: AppDatabase){
        response.body()?.pins?.forEach { i ->
            db.userDao().insert(
                Items(
                    i.id,
                    i.service,
                    i.coordinates.lat,
                    i.coordinates.lng,
                    false
                )
            )
        }
    }
    fun addPinInDataBase(response: Response<PinsInMap>,db: AppDatabase){
        response.body()?.pins?.forEach{ i ->
            if (db.userDao().getAllbyId(i.id) == null){
            db.userDao().insert(
               Items(
                    i.id,
                    i.service,
                    i.coordinates.lat,
                    i.coordinates.lng,
                    false))
             }
            Log.i("Insert new pin in bd","")
        }
    }
}