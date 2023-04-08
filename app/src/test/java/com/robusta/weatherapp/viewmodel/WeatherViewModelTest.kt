package com.robusta.weatherapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.robusta.data.model.location.WeatherData
import com.robusta.domain.usecase.GetSavedWeatherUseCase
import com.robusta.domain.usecase.GetWeatherDataUseCase
import com.robusta.domain.usecase.SaveWeatherUseCase
import com.robusta.weatherapp.repo.FakeWeatherRepo
import com.google.common.truth.Truth.assertThat

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setUp() {
        val fakeWeatherRepo = FakeWeatherRepo()

        val getSavedUseCase = GetSavedWeatherUseCase(fakeWeatherRepo)
        val saveWeatherUseCase = SaveWeatherUseCase(fakeWeatherRepo)
        val getWeatherDataUseCase = GetWeatherDataUseCase(fakeWeatherRepo)
        viewModel = WeatherViewModel(getWeatherDataUseCase,saveWeatherUseCase,getSavedUseCase)
    }

    @Test
    fun getMovies_returnsCurrentList(){
        val weatherList = mutableListOf<WeatherData>()
        weatherList.add(WeatherData( "overview1", "133", "date1", "title1","title1","title1","image1"))

        val currentList = viewModel.getSavedWeather().getOrAwaitValue()
       assertThat(currentList).isEqualTo(weatherList)

    }
}