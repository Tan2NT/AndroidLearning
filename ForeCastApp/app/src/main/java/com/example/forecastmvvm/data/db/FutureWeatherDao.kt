package com.example.forecastmvvm.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecastmvvm.data.db.unitlocalized.future.list.ImprerialFutureWeatherEntry
import com.example.forecastmvvm.data.db.entity.FutureWeatherEntry
import com.example.forecastmvvm.data.db.unitlocalized.future.detail.ImperialSpecificFutureDetailWeather
import org.threeten.bp.LocalDate

@Dao
interface FutureWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherEntry: List<FutureWeatherEntry>)

    @Query("select * from future_weather where date(datetime) >= date(:startDay)")
    fun getSimpleFutureWeatherImperial(startDay: LocalDate): LiveData<List<ImprerialFutureWeatherEntry>>

    @Query("select  * from future_weather where date(datetime) == date(:detailDate)")
    fun getWeatherDetailByDay(detailDate : LocalDate) : LiveData<ImperialSpecificFutureDetailWeather>

    @Query("select count(id) from future_weather where date(datetime) >= date(:startDay)")
    fun countFutureWeather(startDay: LocalDate): Int

    @Query("delete from future_weather where date(datetime) < date(:firstDayToKeep)")
    fun deleteOldEntries(firstDayToKeep: LocalDate)
}