package com.example.forecastmvvm.data.provider

import com.example.forecastmvvm.data.db.entity.WeatherLocation

interface LocationProvider {
    fun hasLocationChanged(lastWeatherLocation: WeatherLocation) : Boolean
    fun getPreferredLocationString() : String
}