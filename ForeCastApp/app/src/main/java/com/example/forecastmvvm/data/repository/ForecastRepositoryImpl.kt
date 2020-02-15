package com.example.forecastmvvm.data.repository

import android.os.Build
import android.util.Log
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

    override suspend fun getCurrentWeather(isImperial : Boolean): LiveData<out ImperialCurrentWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            Log.i("TDebug", "getCurrentWeather 222:")
            return@withContext currentWeatherDao.getWeatherImperial()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather : CurrentWeatherResponse){
        GlobalScope.launch(Dispatchers.IO) {
            Log.i("TDebug", "persistFetchedCurrentWeather: " + fetchedWeather.currentWeatherEntry.toString())
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
        }
    }

    private suspend fun initWeatherData(){
        Log.i("TDebug", "initWeatherData ---- ")
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
        Log.i("TDebug", "TT fetchCurrentWeather for New York" )
        weatherNetworkDataSource.fetchCurrentWeather(
            "New York",
            "en" //Locale.getDefault().language
        )
    }

    private fun isFetchCurrentNeed(lastFetchTime: ZonedDateTime): Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val thrtyMinuteAgo = ZonedDateTime.now().minusMinutes(30)
            return true;//lastFetchTime.isBefore(thrtyMinuteAgo)
        } else {
            return true
        }

    }
}