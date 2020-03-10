package com.example.forecastmvvm.data.network

import android.location.Location
import androidx.lifecycle.LiveData
import com.example.forecastmvvm.data.network.response.CurrentWeatherResponseWeatherbit

interface WeatherNetworkDataSource {
    val downloadCurrentWeather : LiveData<CurrentWeatherResponseWeatherbit>

    suspend fun fetchCurrentWeather(
        isUsingDeviceLocation : Boolean,
        deviceLocation : Location?,
        location: String,
        languageCode : String
    )
}