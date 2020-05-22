package com.tantnt.forecast.ui.weather.current

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tantnt.forecast.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import com.tantnt.forecast.data.provider.UnitProvider
import com.tantnt.forecast.data.repository.ForecastRepository
import com.tantnt.forecast.internal.lazyDeferred
import com.tantnt.forecast.ui.base.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CurrentWeatherViewModel(
    forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    private val TAG : String = "TDebug"

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
        Log.d(TAG, "CurrentWeatherViewModel isImperrial :" + isImperial)
        forecastRepository.getCurrentWeather(isImperial)
    }

    init {
        Log.d(TAG, "CurrentWeatherViewModel Init")
    }

    fun getCurrentWeather() {
        viewModelScope.launch {
            forecastRepository.getCurrentWeather(isImperial)
        }
    }


}
