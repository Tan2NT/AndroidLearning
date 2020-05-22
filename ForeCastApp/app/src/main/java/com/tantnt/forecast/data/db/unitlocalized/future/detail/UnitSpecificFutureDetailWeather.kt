package com.tantnt.forecast.data.db.unitlocalized.future.detail

import org.threeten.bp.LocalDate

interface UnitSpecificFutureDetailWeather {
    val datetime : LocalDate
    val temp: Double
    val icon: String
    val description: String
    val maxTemp: Double
    val minTemp: Double
    val precip: Double
    val validDate: String
    val vis: Double
    val windCdirFull: String
    val windSpd: Double
}