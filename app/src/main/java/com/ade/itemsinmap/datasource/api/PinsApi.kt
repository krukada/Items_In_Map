package com.ade.itemsinmap.datasource.api

data class PinsApi(
    val id:Int = 0,
    val service: String = "",
    val coordinates: Coordinates,
    var visible:Boolean = false
)