package com.example.forecastmvvm.data.db.unitlocalized

import com.google.gson.annotations.SerializedName

interface UnitSpecificCurrentWeatherEntry {
    val feelslike: Int
    val isDay: String
    val precip: Int
    val temperature: Int
    val visibility: Int
    val weatherDescriptions: List<String>
    val weatherIcons: List<String>
    val windDegree: Int
    val windDir: String
    val windSpeed: Int
}