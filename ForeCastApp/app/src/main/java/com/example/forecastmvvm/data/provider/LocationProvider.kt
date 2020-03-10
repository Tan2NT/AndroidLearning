package com.example.forecastmvvm.data.provider
import com.example.forecastmvvm.data.db.unitlocalized.ImperialCurrentWeatherEntry

interface LocationProvider {
    suspend fun hasLocationChanged(imperialCurrentWeatherEntry: ImperialCurrentWeatherEntry) : Boolean
    suspend fun getPreferredLocationString() : String
}