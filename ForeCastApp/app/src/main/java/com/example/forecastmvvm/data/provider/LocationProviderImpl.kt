package com.example.forecastmvvm.data.provider

import com.example.forecastmvvm.data.db.entity.WeatherLocation

class LocationProviderImpl : LocationProvider {
    override fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        return true
    }

    override fun getPreferredLocationString(): String {
        return "DaNang"
    }
}