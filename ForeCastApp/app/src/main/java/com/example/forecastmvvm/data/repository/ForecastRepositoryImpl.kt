package com.example.forecastmvvm.data.repository

import android.os.Build
import androidx.lifecycle.LiveData
import com.example.forecastmvvm.data.db.CurrentWeatherDao
import com.example.forecastmvvm.data.db.unitlocalized.ImperialCurrentWeatherEntry
import com.example.forecastmvvm.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry
import com.example.forecastmvvm.data.network.WeatherNetworkDataSource
import com.example.forecastmvvm.data.network.response.CurrentWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import java.util.*

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,                   // to
    private val weatherNetworkDataSource: WeatherNetworkDataSource
) : ForecastRepository {

    init {
        weatherNetworkDataSource.downloadCurrentWeather.observeForever{ newCurrentWeather ->
            // persist
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getCurrentWeather(): LiveData<out ImperialCurrentWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext currentWeatherDao.getWeatherImperial()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather : CurrentWeatherResponse){
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
        }
    }

    private suspend fun initWeatherData(){
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           if(isFetchCurrentNeed(ZonedDateTime.now().minusHours(1))){
                fetchCurrentWeather()
           }
           else{
               fetchCurrentWeather()
           }
       }
    }

    private suspend fun fetchCurrentWeather(){
        weatherNetworkDataSource.fetchCurrentWeather(
            "Los Angeles",
            Locale.getDefault().language
        )
    }

    private fun isFetchCurrentNeed(lastFetchTime: ZonedDateTime): Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val thrtyMinuteAgo = ZonedDateTime.now().minusMinutes(30)
            return lastFetchTime.isBefore(thrtyMinuteAgo)
        } else {
            return true
        }

    }
}