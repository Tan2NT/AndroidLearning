package com.example.forecastmvvm.data.db.unitlocalized

import androidx.room.ColumnInfo

class ImperialCurrentWeatherEntry(
    @ColumnInfo(name = "feelslike")
    override val feelslike: Int,
    @ColumnInfo(name = "isDay")
    override val isDay: String,
    @ColumnInfo(name = "precip")
    override val precip: Int,
    @ColumnInfo(name = "temperature")
    override val temperature: Int,
    @ColumnInfo(name = "visibility")
    override val visibility: Int,
    @ColumnInfo(name = "weatherDescriptions")
    override val weatherDescriptions: List<String>,
    @ColumnInfo(name = "weatherIcons")
    override val weatherIcons: List<String>,
    @ColumnInfo(name = "windDegree")
    override val windDegree: Int,
    @ColumnInfo(name = "windDir")
    override val windDir: String,
    @ColumnInfo(name = "windSpeed")
    override val windSpeed: Int
) : UnitSpecificCurrentWeatherEntry