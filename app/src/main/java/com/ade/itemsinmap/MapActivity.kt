package com.ade.itemsinmap

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback as Callback1

class MapActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener, Callback1<PinsInMap>{
    private lateinit var mapview: MapView
    private var mapObject:MapObjectCollection? = null
    private var animationHandler: Handler? = null
    private var parameters:PinsInMap? = null
    private val centralMoscow:Point = Point(55.753709, 37.61981)
    private lateinit var db: DataBase.AppDatabase
    private val poins: ArrayList<Coordinates> = ArrayList()


    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, s: String) {
        val pins = parameters?.getCoordinatesInPins(s)

        if (sharedPreferences.getBoolean(s, false)){

            pins?.forEach { i-> db.userDao().insert(DataBase.Coor(null,i.getLat(),i.getLng())) }

            createItemInMap()

        } else if ( !sharedPreferences.getBoolean(s, false)){
            pins?.forEach{ i-> db.userDao().delete(i.getLat(),i.getLng())}

            createItemInMap()

        }
        if (poins.size == 0){
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


    private fun getAllDataBase(): ArrayList<Coordinates> = runBlocking(Dispatchers.IO) {
        //извлечь из дефолтного потока
        val result =
            withContext(Dispatchers.IO) {
                db.userDao().getAll()
            }
        result.forEach { i->
            val coordinate = Coordinates()
            coordinate.setLat(i.lat)
            coordinate.setLng(i.lng)
            poins.add(coordinate)
        }
        return@runBlocking poins
    }

     fun createItemInMap(){
         poins.clear()
         mapObject?.clear()
         val icon = ImageProvider.fromResource(this, R.mipmap.blue_pin)
         getAllDataBase()
         Log.i("Size array of Coordinates", " "+ poins.size)
         poins.forEach{ i->
            val point = mapObject?.addPlacemark(Point(i.getLat(),i.getLng()))
            point?.opacity = 1f
            point?.setIcon(icon)
         }
    }

    fun convertJSONInObject(){
        val factory:ApiService.Factory = ApiService.Factory
        //выполняем aсинхронно
        factory.create().load().enqueue(this)

    }

    override fun onResponse(call: Call<PinsInMap>, response: Response<PinsInMap>) {
        if (response.isSuccessful) {
            parameters = response.body()
            Log.i(R.string.success.toString()," " + response.code())

        } else {
            Log.e(R.string.error.toString() + response.code(), " "+response.errorBody())
        }
    }

    override  fun onFailure(call: Call<PinsInMap>, t: Throwable) {
        t.printStackTrace()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        convertJSONInObject()

        db = Room.databaseBuilder(
            applicationContext,
            DataBase.AppDatabase::class.java, "coords"

        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        MapKitFactory.setApiKey("dd3c95a8-1893-421a-b395-0efad71e8f36")
        MapKitFactory.initialize(this)

        setContentView(R.layout.map_activity)
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_YES
        )

        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        mapview = findViewById(R.id.mapview)


        mapview.map.move(
            CameraPosition(centralMoscow, 12.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 5F),
            null
        )
        mapObject = mapview
            .map
            .mapObjects
            .addCollection()
        animationHandler = Handler()

        createItemInMap()


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
}
