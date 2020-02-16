package com.example.forecastmvvm.data.db.unitlocalized

import com.google.gson.annotations.SerializedName

interface UnitSpecificCurrentWeatherEntry {
    val feelslike: Double
    val isDay: String
    val precip: Double
    val temperature: Double
    val visibility: Double
    val weatherDescriptions: List<String>
    val weatherIcons: List<String>
    val windDegree: Double
    val windDir: String
    val windSpeed: Double
}