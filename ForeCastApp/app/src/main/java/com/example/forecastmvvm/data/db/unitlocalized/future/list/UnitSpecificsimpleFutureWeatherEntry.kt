package com.example.forecastmvvm.data.db.unitlocalized.future.list
import org.threeten.bp.LocalDate

interface UnitSpecificsimpleFutureWeatherEntry {
    val datetime : LocalDate
    val temp: Double
    val icon: String
    val description: String
//    val cityName : String

//    val maxTemp: Double
//    val minTemp: Double
//    val precip: Int
//    val ts: Int
//    val validDate: String
//    val vis: Double
//    val weather: WeatherDescription
//    val windCdirFull: String
//    val windSpd: Double
}