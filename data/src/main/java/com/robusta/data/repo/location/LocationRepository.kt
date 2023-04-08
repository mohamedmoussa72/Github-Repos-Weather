package com.robusta.data.repo.location


import com.robusta.data.model.location.LocationEntity
import io.reactivex.rxjava3.core.Flowable

interface LocationRepository {

    fun getLocation(): Flowable<LocationEntity>

}