package com.robusta.data.repo

import com.robusta.data.model.location.LocationEntity
import com.robusta.data.repo.GoogleLocationDataSource
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val googleLocationDataSource: GoogleLocationDataSource
) : LocationRepository {

    override fun getLocation(): Flowable<LocationEntity> {
        return googleLocationDataSource
            .locationObservable

    }

}