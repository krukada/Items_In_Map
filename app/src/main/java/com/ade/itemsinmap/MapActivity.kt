package com.ade.itemsinmap

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.runtime.image.ImageProvider
import com.google.gson.Gson
import java.io.*

class MapActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    private var mapview: MapView? = null
    private var mapObject:MapObjectCollection? = null
    private var animationHandler: Handler? = null
    private var gson = Gson()
    private var parameters:PinsInMap = PinsInMap()
    private val centralMoscow:Point = Point(55.753709, 37.61981)
    private val key:String = R.string.key.toString()
    private var db: DataBase.AppDatabase? = null


    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, s: String) {
        val pins = parameters.getCoordinatesInPins(s)

        if (sharedPreferences.getBoolean(s, false)){

            pins.forEach { i-> db?.userDao()?.insert(DataBase.Coor(null,i.getLat(),i.getLng())) }

            createItemInMap()

        } else if ( !sharedPreferences.getBoolean(s, false)){
            pins.forEach{ i-> db?.userDao()?.delete(i.getLat(),i.getLng())}

            createItemInMap()


        }
        if (db?.userDao()?.getAll()?.size == 0){
            Toast.makeText(
                this,
                R.string.empty_map,
                Toast.LENGTH_LONG
            ).show()
        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.map_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(@NonNull item: MenuItem): Boolean {
        if (item.itemId == R.id.settings){
            val intent = Intent(this@MapActivity, FilterActivity::class.java)
            startActivity(intent)
        }
        return true
    }

    fun createItemInMap(){
        mapObject!!.clear()
        val icon = ImageProvider.fromResource(this, R.mipmap.blue_pin)
        Log.i("Size array of Coordinates", " "+ db?.userDao()?.getAll()?.size)

        db?.userDao()?.getAll()?.forEach{ i->
            val point = mapObject!!.addPlacemark(Point(i.lat!!,i.lng!!))
            point.opacity = 1f
            point.setIcon(icon)
        }


    }
    fun convertJSONinObject(){

        val file = getAssets().open("pins.json")
        try {
            val br = BufferedReader(InputStreamReader(file))
            parameters = gson.fromJson(br, PinsInMap::class.java)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
            applicationContext,
            DataBase.AppDatabase::class.java, "coords"

        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)


        MapKitFactory.setApiKey(key)
        MapKitFactory.initialize(this)

        setContentView(R.layout.map_activity)
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_YES
        )

        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)

        mapview = findViewById<MapView>(R.id.mapview)
        mapview!!.map.move(
            CameraPosition(centralMoscow, 12.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 5F),
            null
        )
        mapObject = mapview!!
            .getMap()
            .getMapObjects()
            .addCollection()
        animationHandler = Handler()
        createItemInMap()
        convertJSONinObject()

    }
    override fun onStop() {
        super.onStop()
        mapview!!.onStop()
        MapKitFactory.getInstance().onStop()

    }

    override fun onStart() {
        super.onStart()
        mapview!!.onStart()
        MapKitFactory.getInstance().onStart()

    }
}
