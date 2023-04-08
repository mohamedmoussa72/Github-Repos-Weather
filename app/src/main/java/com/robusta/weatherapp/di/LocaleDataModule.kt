package com.robusta.weatherapp.di

import com.robusta.data.locale.WeatherDao
import com.robusta.data.repo.dataSource.WeatherLocaleDataSource
import com.robusta.data.repo.dataSourceImpl.WeatherLocaleDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocaleDataModule {
    @Singleton
    @Provides
    fun provideLocalDataSource(weatherDao: WeatherDao):WeatherLocaleDataSource{
        return WeatherLocaleDataSourceImpl(weatherDao)
    }

}