package com.robusta.data.model.location

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.robusta.data.utile.ConstantDomain.WEATHER_TABLE

@Entity(tableName = WEATHER_TABLE )
data class WeatherData(


    @SerializedName("city_name") var citName: String="",
    @SerializedName("degree") var degree: String="",
    @SerializedName("degree_status") var degreeStatus: String="",
    @SerializedName("degree_min") var degreeMin: String="",
    @SerializedName("degree_max") var degreeMax: String="",
    @SerializedName("wind_speed") var windSpeed: String="",
    @SerializedName("image") var image: String="",
){
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id") var id: Int=0
}