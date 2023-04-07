package com.robusta.weatherapp.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.robusta.data.model.WeatherResponse
import com.robusta.data.utile.Resource
import com.robusta.domain.usecase.GetWeatherDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import androidx.lifecycle.*
import com.robusta.weatherapp.R
import javax.inject.Inject

class WeatherViewModel @Inject constructor(private val getWeatherDataUseCase: GetWeatherDataUseCase):ViewModel() {
    private val weatherData: MutableLiveData<Resource<WeatherResponse>> = MutableLiveData()

    fun getWeatherData(context: Context,lat:Double , lon :Double) = viewModelScope.launch(Dispatchers.IO) {
            weatherData.postValue(Resource.Loading())
        try{
            if(isNetworkAvailable(context)) {

                val apiResult = getWeatherDataUseCase.execute(lat, lon)
                weatherData.postValue(apiResult)
            }else{
                weatherData.postValue(Resource.Error(context.getString(R.string.internet_error_msg)))
            }

        }catch (e: Exception){
            weatherData.postValue(Resource.Error(e.message.toString()))
        }

    }



    private fun isNetworkAvailable(context: Context?):Boolean{
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
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


}