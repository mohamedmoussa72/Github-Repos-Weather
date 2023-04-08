package com.robusta.weatherapp.base

import android.Manifest
import android.content.IntentSender.SendIntentException
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.esafirm.imagepicker.features.registerImagePicker
import com.esafirm.imagepicker.model.Image
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationSettingsRequest
import com.robusta.data.utile.hasLocationPermission
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity(){
    @Inject
   lateinit var locationRequest: LocationRequest
     var imagePath = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        checkLocationPermission()

        super.onCreate(savedInstanceState)
        enableLocationSettings()

    }

    private fun checkLocationPermission() {
        if (! this.hasLocationPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                this.let {
                    ActivityCompat.requestPermissions(
                        it,
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        ),
                        Companion.MY_PERMISSIONS_REQUEST_LOCATION
                    )
                }
            } else {
                this.let {
                    ActivityCompat.requestPermissions(
                        it,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        Companion.MY_PERMISSIONS_REQUEST_LOCATION
                    )
                }
            }
        }

    }

    protected open fun enableLocationSettings() {

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        LocationServices
            .getSettingsClient(this)
            .checkLocationSettings(builder.build())
            .addOnSuccessListener(
                this
            ) { response: LocationSettingsResponse? ->
                Log.e("TagOnnn","ok")
            }
            .addOnFailureListener(
                this
            ) { ex: Exception? ->
                Log.e("TagOnnn","off")
                if (ex is ResolvableApiException) {
                    // Location settings are NOT satisfied,  but this can be fixed  by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),  and check the result in onActivityResult().
                        val resolvable = ex as ResolvableApiException
                        resolvable.startResolutionForResult(
                            this,
                            REQUEST_CODE_CHECK_SETTINGS
                        )
                    } catch (sendEx: SendIntentException) {
                        // Ignore the error.
                    }
                }
            }
    }

    protected open fun startImagePicker(size: Int) {
        val config = ImagePickerConfig {
            limit = size
        }
        launcher.launch(config)
    }
    val launcher = registerImagePicker { result: List<Image> ->

        result.forEach { image ->
            Log.e("TagPath", image.path.toString() + " imagepa") // create new file of compressed path

            imagePath = image.path // get the file path


        }
    }

    companion object {
        const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        const val LOCATION_REQUEST_FASTEST_INTERVAL = 5000L
        val REQUEST_CODE_CHECK_SETTINGS = 12300

    }
}