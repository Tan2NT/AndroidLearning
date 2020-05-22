package com.tantnt.forecast.data.db.entity


import androidx.room.ColumnInfo

class WeatherDescriptionWeatherbit(
    @ColumnInfo(name = "code")
    val code: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "icon")
    val icon: String
)