package com.example.forecastmvvm.ui.weather.future.detail

import androidx.lifecycle.ViewModel
import com.example.forecastmvvm.data.provider.UnitProvider
import com.example.forecastmvvm.data.repository.ForecastRepository
import com.example.forecastmvvm.internal.lazyDeferred
import com.example.forecastmvvm.ui.base.WeatherViewModel
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
