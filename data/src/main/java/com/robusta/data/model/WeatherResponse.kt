package com.robusta.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WeatherResponse(
    @SerializedName("cod")
    val cod: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("main")
    val main: Main?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("weather")
    val weather: List<Weather>?,
    @SerializedName("wind")
    val wind: Wind?
): Serializable