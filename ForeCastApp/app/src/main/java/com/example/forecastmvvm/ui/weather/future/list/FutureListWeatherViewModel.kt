package com.example.forecastmvvm.ui.weather.future.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.forecastmvvm.data.db.unitlocalized.future.list.ImprerialFutureWeatherEntry
import com.example.forecastmvvm.data.provider.UnitProvider
import com.example.forecastmvvm.data.repository.ForecastRepository
import com.example.forecastmvvm.internal.lazyDeferred
import com.example.forecastmvvm.ui.base.WeatherViewModel
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
