package com.robusta.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robusta.data.model.location.LocationEntity
import com.robusta.data.utile.Resource
import com.robusta.domain.usecase.GetLocationUseCase
import com.robusta.domain.usecase.GetSavedWeatherUseCase
import com.robusta.domain.usecase.GetWeatherDataUseCase
import com.robusta.domain.usecase.SaveWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(private val getLocationUseCase: GetLocationUseCase): ViewModel() {
    private val _locationData: MutableLiveData<Resource<LocationEntity>> = MutableLiveData()
    val locationData: LiveData<Resource<LocationEntity>> get()= _locationData
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val _locationModel = MutableLiveData<LocationEntity>()
    val locationModel: LiveData<LocationEntity> = _locationModel

    fun getLocation() {
        compositeDisposable.add(getLocationUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                _locationData.postValue(Resource.Success(it))
                Log.e("TagDAtalo", it.lat.toString())

            })
    }

}