package com.example.forecastmvvm.data.db.unitlocalized

import com.google.gson.annotations.SerializedName

interface UnitSpecificCurrentWeatherEntry {
    val feelslike: Int
    val isDay: String
    val precip: Int
    val temperature: Int
    val visibility: Int
//    @SerializedName("weather_descriptions")
//    val weatherDescriptions: List<String>,
//    @SerializedName("weather_icons")
//    val weatherIcons: List<String>,

    val windDegree: Int
    val windDir: String
    val windSpeed: Int
}