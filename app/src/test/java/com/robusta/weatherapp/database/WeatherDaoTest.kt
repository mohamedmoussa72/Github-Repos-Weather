package com.robusta.weatherapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.robusta.data.locale.WeatherDao
import com.robusta.data.locale.WeatherDataBase
import com.robusta.data.model.location.WeatherData
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class WeatherDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var dao: WeatherDao
    private lateinit var database: WeatherDataBase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDataBase::class.java
        ).build()
        dao = database.getWeatherDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun saveWeatherTest() = runBlocking {
        val weather1 = WeatherData("overview1", "133", "date1", "title1", "title1", "title1", "image1")
        val weather2 = WeatherData("overview2", "135", "date2", "title2", "title2", "title2", "image2")
        val weather3= WeatherData("overview3", "136", "date3", "title3", "title3", "title3", "image3")
        val weatherList = listOf(weather1,weather2,weather3)
        dao.insertData(weather1)
        dao.insertData(weather2)
        dao.insertData(weather3)



        val allMovies = dao.getAllData()
        Truth.assertThat(allMovies).isEqualTo(weatherList)
    }
}
