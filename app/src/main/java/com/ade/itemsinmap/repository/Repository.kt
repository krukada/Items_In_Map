package com.ade.itemsinmap.repository

import android.content.Context
import com.ade.itemsinmap.presenter.Pins


interface Repository {

    fun initDataBase(context: Context)
    fun getPinsInMap()
    fun updateDataBase( visible: Boolean, opacity:Float, s:String)
    fun getAllInDataBase(pins: ArrayList<Pins>):ArrayList<Pins>
}