package com.example.forecastmvvm.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.forecastmvvm.data.db.CurrentWeatherDao
import com.example.forecastmvvm.data.db.FutureWeatherDao
import com.example.forecastmvvm.data.db.unitlocalized.current.ImperialCurrentWeatherEntry
import com.example.forecastmvvm.data.db.unitlocalized.future.detail.UnitSpecificFutureDetailWeather
import com.example.forecastmvvm.data.db.unitlocalized.future.list.UnitSpecificsimpleFutureWeatherEntry
import com.example.forecastmvvm.data.network.WeatherNetworkDataSource
import com.example.forecastmvvm.data.network.response.CurrentWeatherResponseWeatherbit
import com.example.forecastmvvm.data.network.response.FutureWeatherResponseWeatherbit
import com.example.forecastmvvm.data.provider.LocationProvider
import com.example.forecastmvvm.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import java.util.*

const val FORECAST_DAY_COUNT = 16

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val futureWeatherDao : FutureWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider, override var cityName: String
) : ForecastRepository {

    private var lastFetchedCurrentWeatherTime : ZonedDateTime = ZonedDateTime.now().minusHours(1)

    init {
        weatherNetworkDataSource.apply {

            downloadCurrentWeather.observeForever{ newCurrentWeather ->
                // persist
                persistFetchedCurrentWeather(newCurrentWeather)
            }

            downloadFutureWeather.observeForever { newFutureWeather ->
                persistFetchFutureWeather(newFutureWeather)
            }
        }
    }

    override suspend fun getCurrentWeather(isImperial : Boolean): LiveData<out ImperialCurrentWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            Log.i("TDebug", "getCurrentWeather 222:")
            return@withContext currentWeatherDao.getWeatherImperial()
        }
    }

    override suspend fun getFutureWeatherList(startDay: org.threeten.bp.LocalDate, isImperial: Boolean): LiveData<out List<UnitSpecificsimpleFutureWeatherEntry>> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            Log.i("TDebug", "getFutureWEatherList")
            return@withContext futureWeatherDao.getSimpleFutureWeatherImperial(startDay)
        }
    }

    override suspend fun getFutureWeatherDetailByDate(
        date: LocalDate,
        isImperial: Boolean
    ): LiveData<out UnitSpecificFutureDetailWeather> {
       return withContext(Dispatchers.IO){
           Log.i("TDebug", "getFutureWeatherDetailByDate")
           initWeatherData()
           return@withContext futureWeatherDao.getWeatherDetailByDay(date)
       }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather : CurrentWeatherResponseWeatherbit){
        GlobalScope.launch(Dispatchers.IO) {
            Log.i("TDebug", "persistFetchedCurrentWeather: " + fetchedWeather.toString())
            currentWeatherDao.upsert(fetchedWeather.data[0])
        }
    }

    private fun persistFetchFutureWeather(fetchedWeather : FutureWeatherResponseWeatherbit){

        fun deleteOldWeatherEntries(){
            val today = org.threeten.bp.LocalDate.now()
            futureWeatherDao.deleteOldEntries(today)
        }

        GlobalScope.launch(Dispatchers.IO) {
            Log.i("TDebug", "persistFetchedFutureWeather: " + fetchedWeather.toString())
            deleteOldWeatherEntries()
            val futureWeatherList = fetchedWeather.futureWeatherEntries
            futureWeatherDao.upsert( futureWeatherList)
            cityName = fetchedWeather.cityName
        }
    }

    private suspend fun initWeatherData(){
        Log.i("TDebug", "initWeatherData ---- ")

        if(!MainActivity.hasLocationPermission()){
            Log.i("TDebug", "initWeatherData - location permission is NOT granted -> waiting")
            return
        }

        val lastCurrentWeatherEntry = currentWeatherDao.getWeatherImperial().value
        if(lastCurrentWeatherEntry == null || locationProvider.hasLocationChanged(lastCurrentWeatherEntry)){
            if(lastCurrentWeatherEntry == null)
                Log.i("TDebug", "initWeatherData ---- lastCurrentWeatherEntry")

            if(locationProvider.hasLocationChanged(lastCurrentWeatherEntry))
                Log.i("TDebug", "initWeatherData ---- location changed")

            fetchCurrentWeather()
            fetchFutureWeather()
            return;
        }

       if(isFetchCurrentNeed(lastFetchedCurrentWeatherTime)){
           Log.i("TDebug", "initWeatherData ---- need to fetch currenty weather data")
           fetchCurrentWeather()
       }else{
           Log.i("TDebug", "initWeatherData ---- no need to fetch currenty weather data")
       }

        if(isFetchFutureWeatherNeeded()){
            fetchFutureWeather()
        }
    }

    private suspend fun fetchCurrentWeather(){
        Log.i("TDebug", "TT fetchCurrentWeather location: " + locationProvider.getPreferredLocationString() )

        weatherNetworkDataSource.fetchCurrentWeather(
            locationProvider.isUsingDeviceLocation(),
            locationProvider.getDeviceLocation(),
            locationProvider.getPreferredLocationString(),
            Locale.getDefault().language
        )

        lastFetchedCurrentWeatherTime = ZonedDateTime.now()
    }

    private fun isFetchCurrentNeed(lastFetchTime: ZonedDateTime): Boolean{
        val thrtyMinuteAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thrtyMinuteAgo)
    }

    private suspend fun fetchFutureWeather(){
        Log.i("TDebug", "TT fetchCurrentWeather location: " + locationProvider.getPreferredLocationString() )
        weatherNetworkDataSource.fetchFutureWeather(
            locationProvider.isUsingDeviceLocation(),
            locationProvider.getDeviceLocation(),
            locationProvider.getPreferredLocationString(),
            Locale.getDefault().language
        )
    }

    private fun isFetchFutureWeatherNeeded(): Boolean{
        val today = org.threeten.bp.LocalDate.now()
        val forecastDayCount = futureWeatherDao.countFutureWeather(today)
        return forecastDayCount < FORECAST_DAY_COUNT
    }
}