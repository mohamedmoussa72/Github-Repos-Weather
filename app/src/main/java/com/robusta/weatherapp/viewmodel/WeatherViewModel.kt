package com.robusta.weatherapp.viewmodel

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.robusta.data.model.WeatherResponse
import com.robusta.data.utile.Resource
import com.robusta.domain.usecase.GetWeatherDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import androidx.lifecycle.*
import com.robusta.data.model.location.LocationEntity
import com.robusta.data.model.location.WeatherData
import com.robusta.domain.usecase.GetLocationUseCase
import com.robusta.domain.usecase.GetSavedWeatherUseCase
import com.robusta.domain.usecase.SaveWeatherUseCase
import com.robusta.weatherapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val getWeatherDataUseCase: GetWeatherDataUseCase,
                                           private val getLocationUseCase: GetLocationUseCase
                                           ,private val saveWeatherUseCase: SaveWeatherUseCase
                                           ,private val getSavedWeatherUseCase: GetSavedWeatherUseCase):ViewModel() {
    val weatherData: MutableLiveData<Resource<WeatherResponse>> = MutableLiveData()
   private val _locationData: MutableLiveData<Resource<LocationEntity>> = MutableLiveData()
    val locationData: LiveData<Resource<LocationEntity>> get()= _locationData
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun getWeatherData(context: Context, lat: Double, lon: Double) =
        viewModelScope.launch(Dispatchers.IO) {
            weatherData.postValue(Resource.Loading())
            try {
                if (isNetworkAvailable(context)) {
                    Log.e("Tagdddd", lat.toString())
                    val apiResult = getWeatherDataUseCase.execute(lat, lon)
                    Log.e("TagddddAta", apiResult.data?.name.toString())

                    weatherData.postValue(apiResult)
                } else {
                    weatherData.postValue(Resource.Error(context.getString(R.string.internet_error_msg)))
                }

            } catch (e: Exception) {
                weatherData.postValue(Resource.Error(e.message.toString()))
            }

        }

    private fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false

    }

    private val _locationModel = MutableLiveData<LocationEntity>()
    val locationModel: LiveData<LocationEntity> = _locationModel

    fun getLocation() {
        compositeDisposable.add(getLocationUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                _locationData.postValue(Resource.Success(it))
                Log.e("TagDAtalo", it.lat.toString())

            })
    }


    suspend  fun saveArticle(weatherData: WeatherData) = viewModelScope.launch {
        saveWeatherUseCase.execute(weatherData)
    }

     fun getSavedNews() = liveData {
        getSavedWeatherUseCase.execute().collect {
            emit(it)
        }
    }
}