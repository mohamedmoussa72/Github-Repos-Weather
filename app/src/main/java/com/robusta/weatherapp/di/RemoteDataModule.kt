package com.robusta.weatherapp.di

import com.robusta.data.api.ApiService
import com.robusta.data.repo.dataSource.WeatherRemoteDataSource
import com.robusta.data.repo.dataSourceImpl.WeatherRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RemoteDataModule {

    @Provides
    fun provideWeatherRemoteDataSource(
        apiService: ApiService
    ):WeatherRemoteDataSource{
        return WeatherRemoteDataSourceImpl(apiService)
    }

}