package com.robusta.weatherapp.di

import android.app.Application
import androidx.room.Room
import com.robusta.data.locale.WeatherDao
import com.robusta.data.locale.WeatherDataBase
import com.robusta.data.utile.ConstantData.WEATHER_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Singleton
    @Provides
    fun provideWeatherDatabase(app: Application): WeatherDataBase {
        return Room.databaseBuilder(app, WeatherDataBase::class.java, WEATHER_DATABASE)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideWeatherDao(weatherDataBase: WeatherDataBase): WeatherDao {
        return weatherDataBase.getWeatherDao()
    }
}