package com.ade.itemsinmap.mapView

import android.os.Bundle


import com.ade.itemsinmap.R

import com.ade.itemsinmap.presenter.PresenterImpl
import com.ade.itemsinmap.presenter.Presenter


import com.yandex.mapkit.MapKitFactory

import kotlinx.android.synthetic.main.map_activity.*


class Map: MenuActivity() {

    lateinit var presenter: Presenter
    lateinit var view: MapView

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val state = presenter.getState()
        outState.putAll(state)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapKitFactory.setApiKey("dd3c95a8-1893-421a-b395-0efad71e8f36")
        MapKitFactory.initialize(this)
        setContentView(R.layout.map_activity)

         presenter = PresenterImpl(this, savedInstanceState)

         view = MapViewImpl(mapview, presenter as PresenterImpl)

         presenter.attachView(view)

    }

}