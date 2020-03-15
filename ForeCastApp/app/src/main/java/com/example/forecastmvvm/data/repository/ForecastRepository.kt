package com.example.forecastmvvm.data.repository

import androidx.lifecycle.LiveData
import com.example.forecastmvvm.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import com.example.forecastmvvm.data.db.unitlocalized.future.UnitSpecificsimpleFutureWeatherEntry
import org.threeten.bp.LocalDate

interface ForecastRepository {
    suspend fun getCurrentWeather(isImperial : Boolean) : LiveData<out UnitSpecificCurrentWeatherEntry>

    suspend fun getFutureWeatherList(startDay: LocalDate, isImperial: Boolean) : LiveData<out List<UnitSpecificsimpleFutureWeatherEntry>>
}