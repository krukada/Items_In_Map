package com.ade.itemsinmap

class PinsInMap {
    private lateinit var services: Array<String>
    private lateinit var pins:Array<Pins>

    private class Pins{
         var id:Int = 0
         var service: String = ""
         lateinit var coordinates: Coordinates
    }

    fun getCoordinatesInPins(service: String): ArrayList<Coordinates>{
        val coords:ArrayList<Coordinates> = ArrayList()
        pins.forEach {
            i ->  if (i.service == service){
            coords.add(i.coordinates) }
        }
        return coords
    }

}