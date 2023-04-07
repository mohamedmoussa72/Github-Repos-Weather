package com.robusta.weatherapp.di

import android.content.Context
import com.robusta.data.api.ApiService
import com.robusta.data.repo.GoogleLocationDataSource
import com.robusta.data.repo.LocationRepository
import com.robusta.data.repo.LocationRepositoryImpl
import com.robusta.data.repo.dataSource.WeatherRemoteDataSource
import com.robusta.data.repo.dataSourceImpl.WeatherRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocationRepoModule {
    @Singleton
    @Provides
    fun provideLocation(
        googleLocationDataSource: GoogleLocationDataSource
    ): LocationRepository {
        return LocationRepositoryImpl(googleLocationDataSource)
    }

    @Singleton
    @Provides
    fun provideGoogleLocation(
        @ApplicationContext context: Context
    ): GoogleLocationDataSource {
        return GoogleLocationDataSource(context)
    }
}