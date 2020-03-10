package com.example.forecastmvvm.data.network

import androidx.lifecycle.LiveData
import com.example.forecastmvvm.data.network.response.CurrentWeatherResponseWeatherbit

interface WeatherNetworkDataSource {
    val downloadCurrentWeather : LiveData<CurrentWeatherResponseWeatherbit>

    suspend fun fetchCurrentWeather(
        location: String,
        languageCode : String
    )
}