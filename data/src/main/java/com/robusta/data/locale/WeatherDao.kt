package com.robusta.data.locale

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.robusta.data.model.location.WeatherData
import com.robusta.data.utile.ConstantDomain.WEATHER_TABLE
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM $WEATHER_TABLE")
     fun getAllData(): Flow<List<WeatherData>>

    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insertData(locationData: WeatherData)
}