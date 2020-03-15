package com.example.forecastmvvm.ui.weather.current

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.forecastmvvm.data.provider.UnitProvider
import com.example.forecastmvvm.data.repository.ForecastRepository
import com.example.forecastmvvm.internal.UnitSystem
import com.example.forecastmvvm.internal.lazyDeferred
import com.example.forecastmvvm.ui.base.WeatherViewModel

class CurrentWeatherViewModel(
    forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val weather by lazyDeferred(){
        Log.i("TDebug", "CurrentWeatherViewModel isImperrial :" + isImperial)
        forecastRepository.getCurrentWeather(isImperial)
    }
}
