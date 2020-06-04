package com.tantnt.forecast.data.db.unitlocalized.current

import com.tantnt.forecast.data.db.entity.WeatherDescriptionWeatherbit

interface UnitSpecificCurrentWeatherEntry {
    val appTemp: Double
    val cityName: String
    val countryCode: String
    val datetime: String
    val obTime: String
    val precip: Double
    val vis: Double
    val pres: Double
    val temp: Double
    val timezone: String
    val ts: Int
    val lon : Double
    val lat : Double
    //val weatherDescriptionWeatherbit: WeatherDescriptionWeatherbit
    val weatherCode: String
    val weatherDescription: String
    val weatherIcon: String
    val windCdirFull: String
    val windSpd: Double
}