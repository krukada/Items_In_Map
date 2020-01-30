package com.ade.itemsinmap

data class Pins(val id:Int = 0, val service: String = "", val coordinates: Coordinates, var visible:Boolean = false)