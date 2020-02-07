package com.ade.itemsinmap.mapView

import android.content.res.Resources
import android.graphics.BitmapFactory
import com.ade.itemsinmap.R

import com.yandex.runtime.image.ImageProvider

class MyImageProvider(
    private val resources: Resources
): ImageProvider() {
    override fun getId() = "marker_id"
    override fun getImage() = BitmapFactory.decodeResource(resources, R.mipmap.blue_pin)
}