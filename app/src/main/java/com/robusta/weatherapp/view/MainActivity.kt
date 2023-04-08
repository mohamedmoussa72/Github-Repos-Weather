package com.robusta.weatherapp.view

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.robusta.data.model.WeatherResponse
import com.robusta.data.model.location.WeatherData
import com.robusta.data.utile.Resource
import com.robusta.weatherapp.R
import com.robusta.weatherapp.base.BaseActivity
import com.robusta.weatherapp.databinding.ActivityMainBinding
import com.robusta.weatherapp.databinding.DialogLocationDataDetailsBinding
import com.robusta.weatherapp.utile.CompressionUtil
import com.robusta.weatherapp.viewmodel.LocationViewModel
import com.robusta.weatherapp.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private var bitmap: Bitmap? = null
    private lateinit var imageUri: Uri
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val locationViewModel: LocationViewModel by viewModels()
    private val bottomLocationDataDialog by lazy {
        BottomSheetDialog(this)
    }

    @Inject
    lateinit var adapter: WeatherAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingSheet: DialogLocationDataDetailsBinding
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private var isNetworkEnable = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initGUI()
        eventGUI()


    }

    private fun initGUI() {
        weatherViewModel.getSavedWeather()
        binding.apply {
            rvWeatherLocation.adapter = adapter
            rvWeatherLocation.layoutManager = LinearLayoutManager(this@MainActivity)
        }
        locationViewModel.locationData.observe(this) {
            lat = it.data?.lat!!
            lon = it.data?.lon!!
        }

        weatherViewModel.getSavedWeather().observe(this) { result ->
            if (result.isNotEmpty()) {
                binding.ivActivityMainPlaceHolder.visibility = View.GONE
                adapter.add(result)
            } else {
                binding.ivActivityMainPlaceHolder.visibility = View.VISIBLE
            }

        }

        weatherViewModel.weatherData.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if (result.data != null) {
                        setLocationData(result)
                    }

                }
                is Resource.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }

            }

        }


    }

    private fun eventGUI() {
        binding.apply {
            clActivityMainAddWeather.setOnClickListener {
                if (lat != 0.0 && lon != 0.0)
                    startImagePicker(1)
                else
                    Toast.makeText(this@MainActivity,"Application need access location",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        locationViewModel.getLocation()
        launcher.runCatching {
            if (imagePath != "") {
                weatherViewModel.getWeatherData(this@MainActivity, lat, lon)
                showBottomLocationDataDialog()
            }
        }
    }

    private fun showBottomLocationDataDialog() {

        bindingSheet = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_location_data_details,
            null,
            false
        )
        bottomLocationDataDialog.setContentView(bindingSheet.root)
        bindingSheet.pbDialogLocationDataProgressr.visibility = View.VISIBLE
        Glide.with(this@MainActivity).load(imagePath).into(bindingSheet.ivDialogLocationDataImage)

        bindingSheet.apply {
            tvDialogLocationDataShare.setOnClickListener {

                if (bitmap != null) {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "image/*"
                    intent.putExtra(
                        Intent.EXTRA_STREAM,
                        CompressionUtil().getImageUri(this@MainActivity, bitmap!!)
                    )
                    startActivity(intent)
                }
            }
        }

        bottomLocationDataDialog.show()
    }

    private fun setLocationData(result: Resource<WeatherResponse>) {
        bindingSheet.apply {

            pbDialogLocationDataProgressr.visibility = View.GONE
            tvDialogLocationDataCityName.text = result.data?.name.toString()
            tvDialogLocationDataDegree.text = result.data?.main?.temp.toString()
            tvDialogLocationDataMinDegree.text = result.data?.main?.temp_min.toString()
            tvDialogLocationDataMaxDegree.text = result.data?.main?.temp_max.toString()
            tvDialogLocationDataWindSpeed.text = result.data?.wind?.speed.toString()
            tvDialogLocationDataDegreeStatus.text = result.data?.weather?.get(0)?.main.toString()
            
            CoroutineScope(Dispatchers.IO).launch {
                delay(500)
                captureImage(result, clDialogLocationDataContainer)

            }

        }
    }

    private fun captureImage(result: Resource<WeatherResponse>, view: View) {
        bitmap = CompressionUtil().captureView(view)
        imageUri = CompressionUtil().getImageUri(this@MainActivity, bitmap!!)
        saveData(result, imageUri.toString())
    }

    private fun saveData(result: Resource<WeatherResponse>, image: String) {
        CoroutineScope(Dispatchers.IO).launch {
            result.data?.apply {
                weatherViewModel.saveWeather(
                    WeatherData(
                        name.toString(),
                        main?.temp.toString(),
                        weather?.get(0)?.main.toString(),
                        main?.temp_min.toString(),
                        main?.temp_max.toString(),
                        wind?.speed.toString(),
                        image
                    )
                )

            }

        }

    }

    override fun onPause() {
        super.onPause()
        bottomLocationDataDialog.dismiss()
        imagePath = ""

    }
}