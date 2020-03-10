package com.example.forecastmvvm.data.repository

import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.forecastmvvm.data.db.CurrentWeatherDao
import com.example.forecastmvvm.data.db.unitlocalized.ImperialCurrentWeatherEntry
import com.example.forecastmvvm.data.network.WeatherNetworkDataSource
import com.example.forecastmvvm.data.network.response.CurrentWeatherResponseWeatherbit
import com.example.forecastmvvm.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import java.util.*

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,                   // to
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
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

    private fun persistFetchedCurrentWeather(fetchedWeather : CurrentWeatherResponseWeatherbit){
        GlobalScope.launch(Dispatchers.IO) {
            Log.i("TDebug", "persistFetchedCurrentWeather: " + fetchedWeather.toString())
            currentWeatherDao.upsert(fetchedWeather.data[0])
        }
    }

    private suspend fun initWeatherData(){
        Log.i("TDebug", "initWeatherData ---- ")

        val lastCurrentWeatherEntry = currentWeatherDao.getWeatherImperial().value
        if(lastCurrentWeatherEntry == null || locationProvider.hasLocationChanged(lastCurrentWeatherEntry)){
            fetchCurrentWeather()
            return;
        }

       if(isFetchCurrentNeed(ZonedDateTime.now().minusHours(1))){
            fetchCurrentWeather()
       }
    }

    private suspend fun fetchCurrentWeather(){
        Log.i("TDebug", "TT fetchCurrentWeather location: " + locationProvider.getPreferredLocationString() )
        weatherNetworkDataSource.fetchCurrentWeather(
            locationProvider.getPreferredLocationString(),
            Locale.getDefault().language
        )
    }

    private fun isFetchCurrentNeed(lastFetchTime: ZonedDateTime): Boolean{
        val thrtyMinuteAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thrtyMinuteAgo)
    }
}