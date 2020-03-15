package com.example.forecastmvvm.ui.base

import androidx.lifecycle.ViewModel
import com.example.forecastmvvm.data.provider.UnitProvider
import com.example.forecastmvvm.data.repository.ForecastRepository
import com.example.forecastmvvm.internal.UnitSystem

abstract class WeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : ViewModel() {
    // TODO: Implement the ViewModel
    val unitSystem = unitProvider.getUnitSystem()
    val isImperial = unitProvider == UnitSystem.IMPERIAL
}