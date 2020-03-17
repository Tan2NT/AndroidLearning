package com.example.forecastmvvm.data.db.unitlocalized.future.list

import androidx.room.ColumnInfo
import org.threeten.bp.LocalDate

class ImprerialFutureWeatherEntry(
    @ColumnInfo(name = "datetime")
    override val datetime : LocalDate,
    @ColumnInfo(name = "temp")
    override val temp: Double,
    @ColumnInfo(name = "weather_icon")
    override val icon: String,
    @ColumnInfo(name = "weather_description")
    override val description: String
) : UnitSpecificsimpleFutureWeatherEntry