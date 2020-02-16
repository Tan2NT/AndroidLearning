package com.example.forecastmvvm.data.db.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName

const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "current_weather")
data class CurrentWeatherEntry(
    val feelslike: Double,
    @SerializedName("is_day")
    val isDay: String,
    val precip: Double,
    val temperature: Double,
    val visibility: Double,
    @TypeConverters(ListStringConvertor::class)
    @SerializedName("weather_descriptions")
    val weatherDescriptions: List<String> = listOf(),
    @TypeConverters(ListStringConvertor::class)
    @SerializedName("weather_icons")
    val weatherIcons: List<String> = listOf(),
    @SerializedName("wind_degree")
    val windDegree: Double,
    @SerializedName("wind_dir")
    val windDir: String,
    @SerializedName("wind_speed")
    val windSpeed: Double
){
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID
}