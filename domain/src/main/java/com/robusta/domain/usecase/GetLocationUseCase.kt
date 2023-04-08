package com.robusta.domain.usecase

import com.robusta.data.model.location.LocationEntity
import com.robusta.data.repo.location.LocationRepository
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class GetLocationUseCase  @Inject constructor(
    private val locationRepository: LocationRepository
) {

    fun execute() : Flowable<LocationEntity> {
        return locationRepository.getLocation()
    }

}