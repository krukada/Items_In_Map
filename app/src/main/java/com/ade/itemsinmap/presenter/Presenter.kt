package com.ade.itemsinmap.presenter

import android.os.Bundle


import com.ade.itemsinmap.mapView.MapView


import java.util.ArrayList
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject

interface Presenter {

    fun attachView(
        view: MapView)

    fun detachView()

    fun getState(): Bundle

    fun paintPins(
        mapObject: MapObjectCollection?)

    fun pinsUpdate(
        visible: Boolean,
        s: String,
        opacity: Float)

    fun getPlacemarkInMap():ArrayList<PlacemarkMapObject?>

}