package com.robusta.weatherapp.di

import com.robusta.data.repo.WeatherRepository
import com.robusta.data.repo.WeatherRepositoryImpl
import com.robusta.data.repo.dataSource.WeatherLocaleDataSource
import com.robusta.data.repo.dataSource.WeatherRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideWeatherRepository(
        weatherRemoteDataSource: WeatherRemoteDataSource,
        weatherLocaleDataSource: WeatherLocaleDataSource
    ): WeatherRepository {
        return WeatherRepositoryImpl(weatherRemoteDataSource,weatherLocaleDataSource)
    }

}