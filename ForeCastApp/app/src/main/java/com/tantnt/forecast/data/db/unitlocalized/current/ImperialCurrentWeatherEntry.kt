package com.tantnt.forecast.data.db.unitlocalized.current

import androidx.room.ColumnInfo
import com.tantnt.forecast.data.db.entity.WeatherDescriptionWeatherbit

class ImperialCurrentWeatherEntry(
    @ColumnInfo(name = "appTemp")
    override val appTemp: Double,
    @ColumnInfo(name = "cityName")
    override val cityName: String,
    @ColumnInfo(name = "countryCode")
    override val countryCode: String,
    @ColumnInfo(name = "datetime")
    override val datetime: String,
    @ColumnInfo(name = "obTime")
    override val obTime: String,
    @ColumnInfo(name = "precip")
    override val precip: Double,
    @ColumnInfo(name = "vis")
    override val vis: Double,
    @ColumnInfo(name = "pres")
    override val pres: Double,
    @ColumnInfo(name = "temp")
    override val temp: Double,
    @ColumnInfo(name = "timezone")
    override val timezone: String,
    @ColumnInfo(name = "lon")
    override  val lon : Double,
    @ColumnInfo(name = "lat")
    override  val lat : Double,
    @ColumnInfo(name = "ts")
    override val ts: Int,
    @ColumnInfo(name = "weatherDescriptionWeatherbit")
    override val weatherDescriptionWeatherbit: WeatherDescriptionWeatherbit,
    @ColumnInfo(name = "windCdirFull")
    override val windCdirFull: String,
    @ColumnInfo(name = "windSpd")
    override val windSpd: Double
    ) :
    UnitSpecificCurrentWeatherEntry