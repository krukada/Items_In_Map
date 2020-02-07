package com.ade.itemsinmap.mapView

import android.annotation.SuppressLint
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.ade.itemsinmap.MainActivity
import com.ade.itemsinmap.R
import kotlinx.android.synthetic.main.map_activity.*


@SuppressLint("Registered")
open class MenuActivity: MainActivity(){

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.map_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(@NonNull item: MenuItem): Boolean {
        if (item.itemId == R.id.settings){
            this.mapview.visibility = View.INVISIBLE

        }
        if (item.itemId == R.id.goToMap){
            this.mapview.visibility = View.VISIBLE
        }
        return true
    }
}