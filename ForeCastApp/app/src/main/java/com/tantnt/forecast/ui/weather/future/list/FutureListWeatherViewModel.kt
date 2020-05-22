package com.tantnt.forecast.ui.weather.future.list

import com.tantnt.forecast.data.provider.UnitProvider
import com.tantnt.forecast.data.repository.ForecastRepository
import com.tantnt.forecast.internal.lazyDeferred
import com.tantnt.forecast.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class FutureListWeatherViewModel(
    forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val futureWeatherEntries by lazyDeferred {
        forecastRepository.getFutureWeatherList(LocalDate.now(), isImperial)
    }

    fun getRequestedCity() : String {
        return forecastRepository.cityName
    }

}
