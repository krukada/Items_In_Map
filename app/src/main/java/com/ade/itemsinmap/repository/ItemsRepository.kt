package com.ade.itemsinmap.repository

import android.content.Context

import android.util.Log

import androidx.room.Room

import com.ade.itemsinmap.R
import com.ade.itemsinmap.createApiService

import com.ade.itemsinmap.datasource.api.ApiService
import com.ade.itemsinmap.datasource.api.PinsInMap

import com.ade.itemsinmap.datasource.database.AppDatabase
import com.ade.itemsinmap.datasource.database.Items

import com.ade.itemsinmap.presenter.Pins
import com.ade.itemsinmap.presenter.Coordinates


import kotlinx.coroutines.GlobalScope

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


open class ItemsRepository: Repository{
    lateinit var db: AppDatabase


    override fun initDataBase(context: Context) {
        db = Room.databaseBuilder(
              context,
        AppDatabase::class.java, "items"

        ).fallbackToDestructiveMigration().build()
        getPinsInMap()
    }


    override fun updateDataBase( visible: Boolean, opacity:Float, s:String) {
        GlobalScope.launch {
            db.userDao().updateVisiblebyService(s, visible)
        }

    }


    override fun getAllInDataBase(pins: ArrayList<Pins>) =  runBlocking {
        GlobalScope.launch {
            db.userDao().getAll().forEach { i->
                pins.add(
                    Pins(
                        i.uid,
                        i.service,
                        Coordinates(i.lat,i.lng),
                        i.visible)
                )
            }
        }.join()
        return@runBlocking pins
    }

    override fun getPinsInMap(){
        val apiService: ApiService = createApiService()

        apiService.getPinsInMap().enqueue(object : Callback<PinsInMap> {
            override fun onResponse(call: Call<PinsInMap>, response: Response<PinsInMap>) {
                if (response.isSuccessful) {
                    GlobalScope.launch {
                        if (db.userDao().getAll().isEmpty()) {
                            addAllInDataBase(response)
                        } else {
                            addPinInDataBase(response)
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
    fun addAllInDataBase(response: Response<PinsInMap>){
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
    fun addPinInDataBase(response: Response<PinsInMap>){
        response.body()?.pins?.forEach{ i ->
            if (db.userDao().getAllbyId(i.id) == null){
                db.userDao().insert(
                    Items(
                        i.id,
                        i.service,
                        i.coordinates.lat,
                        i.coordinates.lng,
                        false)
                )
            }
            Log.i("Insert new pin in bd","")
        }
    }
}