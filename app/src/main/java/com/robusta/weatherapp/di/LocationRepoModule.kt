package com.robusta.weatherapp.di

import android.content.Context
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.robusta.data.repo.location.GoogleLocationDataSource
import com.robusta.data.repo.location.LocationRepository
import com.robusta.data.repo.location.LocationRepositoryImpl
import com.robusta.weatherapp.base.BaseActivity.Companion.LOCATION_REQUEST_FASTEST_INTERVAL
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
        @ApplicationContext context: Context,locationRequest: LocationRequest
    ): GoogleLocationDataSource {
        return GoogleLocationDataSource(context,locationRequest)
    }

    @Provides
    @Singleton
    fun providesLocationRequest(): LocationRequest {
        return LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, LOCATION_REQUEST_FASTEST_INTERVAL).build()

    }
}