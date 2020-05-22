package com.tantnt.forecast.data.db.unitlocalized.future.detail

import androidx.room.ColumnInfo
import org.threeten.bp.LocalDate

class ImperialSpecificFutureDetailWeather (
    @ColumnInfo(name = "datetime")
    override val datetime : LocalDate,
    @ColumnInfo(name = "temp")
    override val temp: Double,
    @ColumnInfo(name = "weather_icon")
    override val icon: String,
    @ColumnInfo(name = "weather_description")
    override val description: String,
    @ColumnInfo(name = "maxTemp")
    override val maxTemp: Double,
    @ColumnInfo(name = "minTemp")
    override val minTemp: Double,
    @ColumnInfo(name = "precip")
    override  val precip: Double,
    @ColumnInfo(name = "validDate")
    override val validDate: String,
    @ColumnInfo(name = "vis")
    override  val vis: Double,
    @ColumnInfo(name = "windCdirFull")
    override  val windCdirFull: String,
    @ColumnInfo(name = "windSpd")
    override val windSpd: Double
)
    : UnitSpecificFutureDetailWeather