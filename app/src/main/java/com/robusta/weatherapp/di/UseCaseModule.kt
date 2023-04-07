package com.robusta.weatherapp.di

import com.robusta.data.repo.LocationRepository
import com.robusta.data.repo.WeatherRepository
import com.robusta.domain.usecase.GetLocationUseCase
import com.robusta.domain.usecase.GetWeatherDataUseCase
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

}