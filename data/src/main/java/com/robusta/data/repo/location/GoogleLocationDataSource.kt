package com.robusta.data.repo.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.robusta.data.model.location.LocationEntity
import com.robusta.data.utile.hasLocationPermission
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleLocationDataSource @Inject constructor(context: Context, locationRequest: LocationRequest) {

    private val locationSubject = PublishSubject.create<LocationEntity>()
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.locations.forEach(::setLocation)
        }
    }

    val locationObservable: Flowable<LocationEntity> = locationSubject.toFlowable(
        BackpressureStrategy.LATEST
    )
        .doOnSubscribe { startLocationUpdates(context,locationRequest) }
        .doOnCancel { stopLocationUpdates() }


    private fun startLocationUpdates(context: Context, locationRequest: LocationRequest) {

        if (! context.hasLocationPermission()){
         return
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (!isGPSEnabled && !isNetworkEnabled){
           return
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
//        fusedLocationClient.lastLocation.addOnSuccessListener(::setLocation)
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,
            Looper.getMainLooper()
        )
    }



    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun setLocation(location: Location) {
        locationSubject.onNext(
            LocationEntity(
                location.latitude,
                location.longitude
            )
        )
    }

}