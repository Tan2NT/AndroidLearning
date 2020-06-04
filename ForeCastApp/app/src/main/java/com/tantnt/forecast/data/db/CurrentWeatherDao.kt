package com.tantnt.forecast.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tantnt.forecast.data.db.entity.CURRENT_WEATHER_ID
import com.tantnt.forecast.data.db.entity.CurrentWeatherEntry
import com.tantnt.forecast.data.db.unitlocalized.current.ImperialCurrentWeatherEntry

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherEntry: CurrentWeatherEntry)

    @Query("select * from current_weather")
    fun getWeatherImperial(): LiveData<ImperialCurrentWeatherEntry>

    @Query("select * from current_weather")
    fun getCurrentWeatherImperialNonLive(): ImperialCurrentWeatherEntry
}