package com.tantnt.forecast.ui.weather.future.detail

import com.tantnt.forecast.data.provider.UnitProvider
import com.tantnt.forecast.data.repository.ForecastRepository
import com.tantnt.forecast.internal.lazyDeferred
import com.tantnt.forecast.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class FutureDetailWeatherViewModel (
    val date : LocalDate,
    val repository: ForecastRepository,
    val unitProvider: UnitProvider
) : WeatherViewModel(repository, unitProvider) {
    val weather by lazyDeferred {
        repository.getFutureWeatherDetailByDate(date, isImperial)
    }

    fun getRequestedCity() : String {
        return forecastRepository.cityName
    }
}
