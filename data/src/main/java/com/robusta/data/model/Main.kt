package com.robusta.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Main(
    @SerializedName("temp")
    val temp: Double?,
    @SerializedName("temp_max")
    val temp_max: Double?,
    @SerializedName("temp_min")
    val temp_min: Double?
): Serializable