package com.robusta.data.repo.dataSourceImpl

import com.robusta.data.locale.WeatherDao
import com.robusta.data.model.location.WeatherData
import com.robusta.data.repo.dataSource.WeatherLocaleDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherLocaleDataSourceImpl @Inject constructor(private val weatherDao: WeatherDao):WeatherLocaleDataSource{


    override suspend fun saveWeatherToDB(weatherData: WeatherData) {
        weatherDao.insertData(weatherData)
    }

    override fun getSavedWeatherData(): Flow<List<WeatherData>> {
        return weatherDao.getAllData()
    }


}