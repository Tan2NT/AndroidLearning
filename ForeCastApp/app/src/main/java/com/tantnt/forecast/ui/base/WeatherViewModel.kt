package com.tantnt.forecast.ui.base

import androidx.lifecycle.ViewModel
import com.tantnt.forecast.data.provider.UnitProvider
import com.tantnt.forecast.data.repository.ForecastRepository
import com.tantnt.forecast.internal.UnitSystem

abstract class WeatherViewModel(
    val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : ViewModel() {
    // TODO: Implement the ViewModel
    val unitSystem = unitProvider.getUnitSystem()
    val isImperial = unitProvider == UnitSystem.IMPERIAL
}