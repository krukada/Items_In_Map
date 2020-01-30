package com.ade.itemsinmap

import android.graphics.Bitmap
import com.yandex.runtime.image.ImageProvider

class MyImageProvider(val imageId: String, val imageRes: Bitmap): ImageProvider() {
    override fun getId() = imageId
    override fun getImage() = imageRes
}