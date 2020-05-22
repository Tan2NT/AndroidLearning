package com.tantnt.forecast.ui.weather.future.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tantnt.forecast.data.provider.UnitProvider
import com.tantnt.forecast.data.repository.ForecastRepository
import org.threeten.bp.LocalDate

class FutureDetailWeatherViewModalFactory(
    private val date : LocalDate,
    private val forecastRepository : ForecastRepository,
    private val unitProvider: UnitProvider
): ViewModelProvider.NewInstanceFactory (){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FutureDetailWeatherViewModel(date, forecastRepository, unitProvider) as T
    }
}