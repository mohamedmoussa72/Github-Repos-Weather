package com.robusta.weatherapp.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.robusta.data.model.WeatherResponse
import com.robusta.data.utile.Resource
import com.robusta.domain.usecase.GetWeatherDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import androidx.lifecycle.*
import com.robusta.data.model.location.WeatherData
import com.robusta.domain.usecase.GetSavedWeatherUseCase
import com.robusta.domain.usecase.SaveWeatherUseCase
import com.robusta.weatherapp.R
import com.robusta.weatherapp.utile.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val getWeatherDataUseCase: GetWeatherDataUseCase,
                                           private val saveWeatherUseCase: SaveWeatherUseCase,
                                           private val getSavedWeatherUseCase: GetSavedWeatherUseCase):ViewModel() {
    val weatherData: MutableLiveData<Resource<WeatherResponse>> = MutableLiveData()

    fun getWeatherData(context: Context, lat: Double, lon: Double) =
        viewModelScope.launch(Dispatchers.IO) {
            weatherData.postValue(Resource.Loading())
            try {
                if (context.isNetworkAvailable(context)) {
                    val apiResult = getWeatherDataUseCase.execute(lat, lon)
                    weatherData.postValue(apiResult!!)
                } else {
                    weatherData.postValue(Resource.Error(context.getString(R.string.internet_error_msg)))
                }

            } catch (e: Exception) {
                weatherData.postValue(Resource.Error(e.message.toString()))
            }

        }


    suspend  fun saveWeather(weatherData: WeatherData) = viewModelScope.launch {
        saveWeatherUseCase.execute(weatherData)
    }

     fun getSavedWeather() = liveData {
        getSavedWeatherUseCase.execute().collect {
            emit(it)
        }
    }
}