package com.robusta.weatherapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

abstract class  BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                Log.e(TAG, "onViewCreated" + " gps off")
            }else{
                Log.e(TAG, "onViewCreated" + " gps on")
            }


        }
    }

    open fun isLocationEnabled(context: Context): Boolean {
        val mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Checking GPS is enabled
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    companion object {
        private const val TAG = "BaseActivity"
    }

}