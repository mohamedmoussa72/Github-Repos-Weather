package com.robusta.data.repo.location

import com.robusta.data.model.location.LocationEntity
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