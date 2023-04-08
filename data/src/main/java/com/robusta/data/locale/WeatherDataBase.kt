package com.robusta.data.locale

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.robusta.data.model.location.WeatherData

@Database(entities = [WeatherData::class],
    version = 2, exportSchema = false)
abstract class WeatherDataBase : RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao


    companion object {
        private const val TAG = "AppDatabase"
        private val LOCK = Any()
        private const val DATABASE_NAME = "Weather.db"
        var instance: WeatherDataBase? = null
            private set

        @JvmStatic
        fun initializeDataBase(context: Context?): WeatherDataBase? {
            if (instance == null) {
                synchronized(LOCK) {
                    instance = Room.databaseBuilder(context!!,
                        WeatherDataBase::class.java,
                        DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance
        }
    }
}