package com.robusta.data.repo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.robusta.data.model.location.LocationEntity
import com.robusta.data.utile.LocationException
import com.robusta.data.utile.Resource
import com.robusta.data.utile.hasLocationPermission
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton
private const val LOCATION_REQUEST_INTERVAL = 10000L
private const val LOCATION_REQUEST_FASTEST_INTERVAL = 5000L
private const val LOCATION_REQUEST_FASTEST_INTER = 5000

@Singleton
class GoogleLocationDataSource @Inject constructor(context: Context) {

    private val locationSubject = PublishSubject.create<LocationEntity>()
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest: LocationRequest =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, LOCATION_REQUEST_FASTEST_INTERVAL).build()

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.e("Taglllsddds","gg")

            locationResult.locations.forEach(::setLocation)
        }
    }

    val locationObservable: Flowable<LocationEntity> = locationSubject.toFlowable(
        BackpressureStrategy.MISSING
    )
        .doOnSubscribe { startLocationUpdates(context) }
        .doOnCancel { stopLocationUpdates() }


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates(context: Context) {
        Log.e("Taglll",context.toString())


        if (! context.hasLocationPermission()){
           throw LocationException("Missing location")
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (!isGPSEnabled && !isNetworkEnabled){
            throw LocationException("Missing GPS")
        }

        fusedLocationClient.lastLocation.addOnSuccessListener(::setLocation)
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,
            Looper.getMainLooper()
        )
    }



    private fun stopLocationUpdates() {
        Log.e("Taglllss","gg")

        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun setLocation(location: Location) {
        Log.e("Taglll",location.latitude.toString())
        locationSubject.onNext(
            LocationEntity(
                location.latitude,
                location.longitude
            )
        )
    }

}
