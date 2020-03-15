package com.example.forecastmvvm.data.network

import android.location.Location
import androidx.lifecycle.LiveData
import com.example.forecastmvvm.data.network.response.CurrentWeatherResponseWeatherbit
import com.example.forecastmvvm.data.network.response.FutureWeatherResponseWeatherbit

interface WeatherNetworkDataSource {
    val downloadCurrentWeather : LiveData<CurrentWeatherResponseWeatherbit>
    val downloadFutureWeather : LiveData<FutureWeatherResponseWeatherbit>

    suspend fun fetchCurrentWeather(
        isUsingDeviceLocation : Boolean,
        deviceLocation : Location?,
        location: String,
        languageCode : String
    )

    suspend fun fetchFutureWeather(
        isUsingDeviceLocation : Boolean,
        deviceLocation : Location?,
        location: String,
        languageCode : String
    )
}