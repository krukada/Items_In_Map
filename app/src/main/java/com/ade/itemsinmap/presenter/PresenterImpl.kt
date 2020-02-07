package com.ade.itemsinmap.presenter
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceManager
import com.ade.itemsinmap.mapView.MapView

import com.ade.itemsinmap.repository.ItemsRepository

import java.util.ArrayList

import com.ade.itemsinmap.repository.Repository
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject



class PresenterImpl(context: Context ,savedState: Bundle?): SharedPreferences.OnSharedPreferenceChangeListener ,Presenter {

    private val BUNDLE_KEY_PINS = "key"

    val repository: Repository = ItemsRepository()

    val bundle = savedState

    private val placemarks: ArrayList<PlacemarkMapObject?> = ArrayList()

    var pins:  ArrayList<Pins> = ArrayList()




    private var view: MapView? = null

    init {
        repository.initDataBase(context)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun getState(): Bundle  {
        val bungel = Bundle()
        bungel.putParcelableArrayList(BUNDLE_KEY_PINS,pins)
        return bungel
    }

    override fun attachView(view: MapView) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }


    fun getPinsInDataBase() {
        repository.getAllInDataBase(pins)
    }

    override fun getPlacemarkInMap(): ArrayList<PlacemarkMapObject?> {
        return placemarks
    }

    override fun pinsUpdate(visible: Boolean, s: String, opacity: Float) {
        repository.updateDataBase(visible,opacity,s)
        pins.forEach { i ->
            if (i.service == s) {
                i.visible = visible
                placemarks[i.id - 1]?.opacity = opacity
            }
        }


    }

    override fun paintPins(mapObject: MapObjectCollection?) {
        if (bundle != null) {
            if (bundle.containsKey(BUNDLE_KEY_PINS)) {
                pins = bundle.getParcelableArrayList(BUNDLE_KEY_PINS)!!
            }

        }

        if (pins.size == 0) {
            getPinsInDataBase()

        }

        pins.forEach{ i->
            val point = mapObject?.addPlacemark(Point(i.coordinates.lat,i.coordinates.lng))
            if (i.visible) {
                point?.opacity = 1f
            } else
                point?.opacity = 0f
            placemarks.add(point)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, s: String) {
        if (sharedPreferences.getBoolean(s, false)) {
            pinsUpdate(true, s,opacity = 1f)
        } else {
            pinsUpdate(false, s,opacity = 0f)
        }
    }

}