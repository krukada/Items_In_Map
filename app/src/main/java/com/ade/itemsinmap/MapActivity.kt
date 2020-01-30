package com.ade.itemsinmap

import android.Manifest
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.*


open class MapActivity : MenuActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    val centralMoscow:Point = Point(55.753709, 37.61981)

    private val pins: ArrayList<Pins> = ArrayList()
    private val placemarks: ArrayList<PlacemarkMapObject?> = ArrayList()

    private lateinit var db: AppDatabase
    private lateinit var mapview: MapView

    private var mapObject:MapObjectCollection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapKitFactory.setApiKey("dd3c95a8-1893-421a-b395-0efad71e8f36")
        MapKitFactory.initialize(this)

        setContentView(R.layout.map_activity)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "items"

        ).fallbackToDestructiveMigration().build()
        convertJSON()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_YES
        )
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        addMapInMapActivity()

        addPinsOnMap()


    }
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, s: String) {
        if (sharedPreferences.getBoolean(s, false)) {
            updateDataBase(true,1f,s)
        } else {
            updateDataBase(false,0f,s)
        }
        if (pins.size == 0) {
            Toast.makeText(
                this,
                R.string.empty_map,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    override fun onStop() {
        super.onStop()
        mapview.onStop()
        MapKitFactory.getInstance().onStop()

    }

    override fun onStart() {
        super.onStart()
        mapview.onStart()
        MapKitFactory.getInstance().onStart()

    }

    fun addMapInMapActivity(){
        mapview = findViewById(R.id.mapview)
        mapview.map.move(
            CameraPosition(centralMoscow, 12.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 5F),
            null
        )
        mapObject = mapview.map.mapObjects.addCollection()
    }

    fun addIconOnPin():ImageProvider{
        val bitmap =  BitmapFactory.decodeResource(resources, R.mipmap.blue_pin)
        val info =
            String.format("Info: size = %s x %s, bytes = %s (%s), config = %s",
                bitmap.width,
                bitmap.height,
                bitmap.byteCount,
                bitmap.rowBytes,
                bitmap.config)

        return MyImageProvider(info,bitmap)
    }

    fun addPinsOnMap(){

        val imageProvider = addIconOnPin()
        getAllDataBase()

        pins.forEach{ i->
            val point = mapObject?.addPlacemark(Point(i.coordinates.lat,i.coordinates.lng))
            if (i.visible) {
                point?.opacity = 1f
            } else
                point?.opacity = 0f
            point?.setIcon(imageProvider)
            placemarks.add(point)
         }
    }

    fun updateDataBase(visible: Boolean, opacity:Float, s:String){
        GlobalScope.launch {
            db.userDao().updateVisiblebyService(s, visible)
        }
        pins.forEach { i ->
            if (i.service == s) {
                i.visible = true
                placemarks[i.id - 1]?.opacity = opacity
            }
        }

    }

    fun getAllDataBase(): ArrayList<Pins> = runBlocking {
        GlobalScope.launch {
            db.userDao().getAll().forEach { i->
                pins.add(Pins(
                    i.uid,
                    i.service,
                    Coordinates(i.lat,i.lng),
                    i.visible)
                )
            }
        }.join()
        return@runBlocking pins
    }

    fun convertJSON(){
        val factory = PinsRepository()
        factory.getPinsInMap(db)
        Thread.sleep(2000)
    }
}
