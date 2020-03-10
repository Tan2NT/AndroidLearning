package com.example.forecastmvvm.data.db.entity


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

class WeatherDescriptionWeatherbit(
    @ColumnInfo(name = "code")
    val code: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "icon")
    val icon: String
)