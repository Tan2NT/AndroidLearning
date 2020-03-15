package com.example.forecastmvvm.data.network.response


import androidx.room.*
import com.example.forecastmvvm.data.db.entity.LocalDateTimeConverter
import com.google.gson.annotations.SerializedName

@Entity(tableName = "future_weather", indices = [Index(value = ["datetime"], unique = true)])
data class FutureWeatherEntry(
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null,
    @TypeConverters(LocalDateTimeConverter::class)
    val datetime : String,
    @SerializedName("app_max_temp")
    val appMaxTemp: Double,
    @SerializedName("app_min_temp")
    val appMinTemp: Double,
    @SerializedName("high_temp")
    val highTemp: Double,
    @SerializedName("low_temp")
    val lowTemp: Double,
    @SerializedName("max_temp")
    val maxTemp: Double,
    @SerializedName("min_temp")
    val minTemp: Double,
    val precip: Double,
    val temp: Double,
    val ts: Int,
    @SerializedName("valid_date")
    val validDate: String,
    val vis: Double,
    @Embedded (prefix = "weather_")
    val weather: WeatherDescription,
    @SerializedName("wind_cdir_full")
    val windCdirFull: String,
    val windSpd: Double
)