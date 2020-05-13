package com.example.forecastmvvm.data.repository

import androidx.lifecycle.LiveData
import com.example.forecastmvvm.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import com.example.forecastmvvm.data.db.unitlocalized.future.detail.UnitSpecificFutureDetailWeather
import com.example.forecastmvvm.data.db.unitlocalized.future.list.UnitSpecificsimpleFutureWeatherEntry
import org.threeten.bp.LocalDate

interface ForecastRepository {

    var cityName: String

    fun getRequestedCity() : String {
        return cityName
    }

    suspend fun getCurrentWeather(isImperial : Boolean) : LiveData<out UnitSpecificCurrentWeatherEntry>

    suspend fun getFutureWeatherList(startDay: LocalDate, isImperial: Boolean) : LiveData<out List<UnitSpecificsimpleFutureWeatherEntry>>

    suspend fun getFutureWeatherDetailByDate(date: LocalDate, isImperial: Boolean) : LiveData<out UnitSpecificFutureDetailWeather>
}