package com.tantnt.forecast.ui.weather.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tantnt.forecast.data.provider.UnitProvider
import com.tantnt.forecast.data.repository.ForecastRepository

class CurrentWeatherViewModalFactory(
    private val forecastRepository : ForecastRepository,
    private val unitProvider: UnitProvider
): ViewModelProvider.NewInstanceFactory (){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(forecastRepository, unitProvider) as T
    }
}