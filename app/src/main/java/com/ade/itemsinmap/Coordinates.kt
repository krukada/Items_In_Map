package com.ade.itemsinmap

class Coordinates{
    private var lat:Double = 0.0
    private var lng:Double = 0.0

    fun getLat():Double{
        return lat
    }
    fun getLng():Double{
        return lng
    }
    fun setLat(lat: Double){
        this.lat = lat
    }
    fun setLng(lng: Double){
        this.lng = lng
    }
}