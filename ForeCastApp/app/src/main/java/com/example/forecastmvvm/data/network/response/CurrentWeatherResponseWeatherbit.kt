package com.example.forecastmvvm.data.network.response

import com.example.forecastmvvm.data.db.entity.CurrentWeatherEntry


data class CurrentWeatherResponseWeatherbit(
    val count: Int,
    val `data`: List<CurrentWeatherEntry>
)