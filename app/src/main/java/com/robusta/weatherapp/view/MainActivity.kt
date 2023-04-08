package com.robusta.weatherapp.view

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
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
    private lateinit var bitmap :Bitmap
    private lateinit var imageUri: Uri
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val locationViewModel: LocationViewModel by viewModels()
    private val bottomLocationDataDialog by lazy  {
        BottomSheetDialog( this)
    }
    @Inject
    lateinit var adapter: WeatherAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingSheet: DialogLocationDataDetailsBinding
    private var lat: Double = 0.0
    private var lon: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initGUI()
        eventGUI()


    }

    private fun initGUI() {
        binding.apply {
            rvWeatherLocation.adapter = adapter
            rvWeatherLocation.layoutManager = LinearLayoutManager(this@MainActivity)
        }
        locationViewModel.locationData.observe(this) {
            lat = it.data?.lat!!
            lon = it.data?.lon!!
            Log.e("TagDAtalo", lat.toString())
        }

        weatherViewModel.getSavedWeather().observe(this) { result ->
            adapter.add(result)
        }

        weatherViewModel.weatherData.observe(this) { result ->
            if (result.data != null ) {
                setLocationData(result)
            }

        }


    }

    private fun eventGUI() {
        binding.apply {
            clActivityMainAddWeather.setOnClickListener {
                startImagePicker(1)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        locationViewModel.getLocation()
        launcher.runCatching {
            Log.e("TagImage",imagePath)
            if(imagePath !="") {
                Log.e("TagImageopen",imagePath)
                weatherViewModel.getWeatherData(this@MainActivity, lat, lon)
                showBottomLocationDataDialog()
            }
        }    }

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


                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_STREAM, CompressionUtil().getImageUri(this@MainActivity, bitmap))
                startActivity(intent)
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
            result.data?.weather.let {
                tvDialogLocationDataDegreeStatus.text =
                    result.data?.weather?.get(0)?.main.toString()

            }
            CoroutineScope(Dispatchers.IO).launch {
                delay(500)
                captureImage(result,clDialogLocationDataContainer)

        }

        }
    }
       private fun captureImage(result: Resource<WeatherResponse>, view: View){
           bitmap =CompressionUtil().captureView( view)
           imageUri = CompressionUtil().getImageUri(this@MainActivity, bitmap)
           saveData(result,imageUri.toString())
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
        imagePath=""

    }
}