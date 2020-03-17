package com.example.forecastmvvm.data.db.entity


import com.google.gson.annotations.SerializedName

data class WeatherDescription(
    val code: Int,
    val description: String,
    val icon: String
)