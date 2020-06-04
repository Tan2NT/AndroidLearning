package com.tantnt.forecast.data.network.response

import com.tantnt.forecast.data.db.entity.CurrentWeatherEntry


data class CurrentWeatherResponseWeatherbit(
    val count: Int,
    val data: List<CurrentWeatherEntry>
)