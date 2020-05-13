package com.example.forecastmvvm.ui.weather.current

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.forecastmvvm.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import com.example.forecastmvvm.data.provider.UnitProvider
import com.example.forecastmvvm.data.repository.ForecastRepository
import com.example.forecastmvvm.data.repository.ForecastRepositoryImpl
import com.example.forecastmvvm.internal.UnitSystem
import com.example.forecastmvvm.internal.lazyDeferred
import com.example.forecastmvvm.ui.base.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CurrentWeatherViewModel(
    forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = SupervisorJob()

    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     *
     * Since we pass viewModelJob, you can cancel all coroutines launched by uiScope by calling
     * viewModelJob.cancel()
     */
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _currentWeather = MutableLiveData<UnitSpecificCurrentWeatherEntry>()
    val currentWeather : LiveData<out UnitSpecificCurrentWeatherEntry>
        get() = _currentWeather

    val weather by lazyDeferred(){
        Log.i("TDebug", "CurrentWeatherViewModel isImperrial :" + isImperial)
        forecastRepository.getCurrentWeather(isImperial)
    }

    init {
        Log.i("TDebug", "CurrentWeatherViewModel Init")
    }

    fun getCurrentWeather() {
        viewModelScope.launch {
            forecastRepository.getCurrentWeather(isImperial)
        }
    }


}
