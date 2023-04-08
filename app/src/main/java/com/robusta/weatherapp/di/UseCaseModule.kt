package com.robusta.weatherapp.di

import com.robusta.data.repo.location.LocationRepository
import com.robusta.data.repo.WeatherRepository
import com.robusta.domain.usecase.GetLocationUseCase
import com.robusta.domain.usecase.GetSavedWeatherUseCase
import com.robusta.domain.usecase.GetWeatherDataUseCase
import com.robusta.domain.usecase.SaveWeatherUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideWeatherRemoteUseCase(
        weatherRepository: WeatherRepository
    ):GetWeatherDataUseCase{
        return GetWeatherDataUseCase(weatherRepository)
    }

    @Provides
    fun provideLocationUseCase(
        locationRepository: LocationRepository
    ):GetLocationUseCase{
        return GetLocationUseCase(locationRepository)
    }

    @Singleton
    @Provides
    fun provideSaveLocationDataUseCase(
        newsRepository: WeatherRepository
    ):SaveWeatherUseCase{
        return SaveWeatherUseCase(newsRepository)
    }

    @Singleton
    @Provides
    fun provideGetSavedLocationDataUseCase(
        newsRepository: WeatherRepository
    ):GetSavedWeatherUseCase{
        return GetSavedWeatherUseCase(newsRepository)
    }

}