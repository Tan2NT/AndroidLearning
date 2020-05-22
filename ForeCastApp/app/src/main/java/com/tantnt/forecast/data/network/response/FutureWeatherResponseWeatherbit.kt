package com.tantnt.forecast.data.network.response


import com.tantnt.forecast.data.db.entity.FutureWeatherEntry
import com.google.gson.annotations.SerializedName

data class FutureWeatherResponseWeatherbit(
    @SerializedName("city_name")
    val cityName: String,
    @SerializedName("country_code")
    val countryCode: String,
    @SerializedName("data")
    val `futureWeatherEntries`: List<FutureWeatherEntry>,
    val lat: String,
    val lon: String,
    @SerializedName("state_code")
    val stateCode: String,
    val timezone: String
)