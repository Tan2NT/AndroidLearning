package com.example.forecastmvvm.ui.weather.future.detail

import com.example.forecastmvvm.ui.weather.future.detail.FutureDetailWeatherViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forecastmvvm.data.provider.UnitProvider
import com.example.forecastmvvm.data.repository.ForecastRepository
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