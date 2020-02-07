package com.ade.itemsinmap.mapView

import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.ade.itemsinmap.R
import com.ade.itemsinmap.presenter.PresenterImpl
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection

interface MapView {
     fun updateModeNight()
     fun addMapInActivity(mapView: com.yandex.mapkit.mapview.MapView, central: Point)
     fun addItemInPin(presenter: PresenterImpl)

}

class MapViewImpl(view: View,presenter: PresenterImpl): MapView {

    private val centralMoscow:Point = Point(55.753709, 37.61981)

    private val imageProvider = MyImageProvider(view.resources)

    val mapView: com.yandex.mapkit.mapview.MapView = view.findViewById(R.id.mapview)

    var mapObject: MapObjectCollection = mapView.map.mapObjects.addCollection()


    init {
        updateModeNight()
        addMapInActivity(mapView, centralMoscow)
        addItemInPin(presenter)
    }

    override fun updateModeNight() {
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_YES
        )
    }

    override fun addMapInActivity(mapView: com.yandex.mapkit.mapview.MapView, central: Point) {
        mapView.map.move(
            CameraPosition(central, 12.0f, 0.0f, 0.0f),
            Animation(Animation.Type.LINEAR, 0F),
            null
        )
        mapObject = mapView.map.mapObjects.addCollection()

    }

    override fun addItemInPin(presenter: PresenterImpl) {
        presenter.paintPins(mapObject)
        presenter.getPlacemarkInMap().forEach {
                i->i?.setIcon(imageProvider)
        }
    }

}
