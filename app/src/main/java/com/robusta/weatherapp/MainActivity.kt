package com.robusta.weatherapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.robusta.data.model.WeatherResponse
import com.robusta.data.model.location.WeatherData
import com.robusta.data.utile.Resource
import com.robusta.weatherapp.base.BaseActivity
import com.robusta.weatherapp.databinding.ActivityMainBinding
import com.robusta.weatherapp.databinding.DialogLocationDataDetailsBinding
import com.robusta.weatherapp.view.WeatherAdapter
import com.robusta.weatherapp.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private var compressedPath = ""
    private val viewModel: WeatherViewModel by viewModels()
    private var bottomLocationDataDialog: BottomSheetDialog? = null

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
        viewModel.locationData.observe(this) {
            lat = it.data?.lat!!
            lon = it.data?.lon!!
        }

        viewModel.getSavedNews().observe(this) { result ->
            adapter.add(result)
        }

        viewModel.weatherData.observe(this) { result ->
            setLocationData(result)
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
        viewModel.getLocation()
        if (imagePath != "")
            showBottomLocationDataDialog()

    }

    private fun showBottomLocationDataDialog() {
        bottomLocationDataDialog = BottomSheetDialog(this)
        bindingSheet = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_location_data_details,
            null,
            false
        )
        bottomLocationDataDialog?.setContentView(bindingSheet.root)
        bindingSheet.pbDialogLocationDataProgressr.visibility = View.VISIBLE
        viewModel.getWeatherData(this@MainActivity, lat, lon)
        bindingSheet.apply {
            tvDialogLocationDataShare.setOnClickListener {

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_STREAM, compressedPath.toUri())
                startActivity(intent)
            }
        }

        bottomLocationDataDialog?.show()
    }

    private fun setLocationData(result: Resource<WeatherResponse>) {
        bindingSheet.apply {
            Glide.with(this@MainActivity).load(imagePath).into(ivDialogLocationDataImage)

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


//             compressedPath = CompressionUtil().getBitmapFromView(this@MainActivity, clDialogLocationDataContainer)
//            saveData(result,compressedPath)
            Log.e("TagUri", compressedPath.toString())
        }
    }


    private fun saveData(result: Resource<WeatherResponse>, image: String) {
        CoroutineScope(Dispatchers.IO).launch {
            result.data?.apply {
                viewModel.saveArticle(
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

}