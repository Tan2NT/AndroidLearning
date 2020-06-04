package com.tantnt.forecast.data.db.entity


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName

const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "current_weather")
data class CurrentWeatherEntry(
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID,
    @SerializedName("app_temp")
    val appTemp: Double,
    @SerializedName("city_name")
    val cityName: String,
    @SerializedName("country_code")
    val countryCode: String,
    val datetime: String,
    @SerializedName("ob_time")
    val obTime: String,
    val precip: Double,
    val vis: Double,
    val pres: Double,
    val temp: Double,
    val timezone: String,
    val lon : Double,
    val lat: Double,
    val ts: Int,
    @TypeConverters(DataTypeConvertor::class)
    @SerializedName("weather")
    @Embedded(prefix = "weather_")
    val weatherDescriptionWeatherbit: WeatherDescriptionWeatherbit,
    @SerializedName("wind_cdir_full")
    val windCdirFull: String,
    @SerializedName("wind_spd")
    val windSpd: Double
){
}
