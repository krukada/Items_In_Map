package com.ade.itemsinmap.mapView

import android.content.SharedPreferences
import android.os.Bundle

import androidx.annotation.Nullable

import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.ade.itemsinmap.R

class SettingsFragment : PreferenceFragmentCompat() , SharedPreferences.OnSharedPreferenceChangeListener{
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {}

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        sharedPreferences.registerOnSharedPreferenceChangeListener( this)
        addPreferencesFromResource(R.xml.root_preferences)

    }
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, s: String) {}
    override fun onDestroy() {
        super.onDestroy()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
}
