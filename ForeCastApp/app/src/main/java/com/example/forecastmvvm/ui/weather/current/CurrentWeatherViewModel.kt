package com.example.forecastmvvm.ui.weather.current

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.forecastmvvm.data.provider.UnitProvider
import com.example.forecastmvvm.data.repository.ForecastRepository
import com.example.forecastmvvm.internal.UnitSystem
import com.example.forecastmvvm.internal.lazyDeferred

class CurrentWeatherViewModel(
    forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : ViewModel() {
    private val unitSystem = unitProvider.getUnitSystem()

    val isImperial : Boolean
        get() = unitSystem == UnitSystem.IMPERIAL

    val weather by lazyDeferred(){
        Log.i("TDebug", "CurrentWeatherViewModel isImperrial :" + isImperial)
        forecastRepository.getCurrentWeather(isImperial)
    }
}
