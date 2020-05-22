package com.tantnt.forecast.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.tantnt.forecast.data.db.CurrentWeatherDao
import com.tantnt.forecast.data.db.FutureWeatherDao
import com.tantnt.forecast.data.db.unitlocalized.current.ImperialCurrentWeatherEntry
import com.tantnt.forecast.data.db.unitlocalized.future.detail.UnitSpecificFutureDetailWeather
import com.tantnt.forecast.data.db.unitlocalized.future.list.UnitSpecificsimpleFutureWeatherEntry
import com.tantnt.forecast.data.network.WeatherNetworkDataSource
import com.tantnt.forecast.data.network.response.CurrentWeatherResponseWeatherbit
import com.tantnt.forecast.data.network.response.FutureWeatherResponseWeatherbit
import com.tantnt.forecast.data.provider.LocationProvider
import com.tantnt.forecast.ui.MainActivity
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

    private val TAG : String = "TDebug"
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
            Log.d(TAG, "getCurrentWeather 222:")
            return@withContext currentWeatherDao.getWeatherImperial()
        }
    }

    override suspend fun getFutureWeatherList(startDay: org.threeten.bp.LocalDate, isImperial: Boolean): LiveData<out List<UnitSpecificsimpleFutureWeatherEntry>> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            Log.d(TAG, "getFutureWEatherList")
            return@withContext futureWeatherDao.getSimpleFutureWeatherImperial(startDay)
        }
    }

    override suspend fun getFutureWeatherDetailByDate(
        date: LocalDate,
        isImperial: Boolean
    ): LiveData<out UnitSpecificFutureDetailWeather> {
       return withContext(Dispatchers.IO){
           Log.d(TAG, "getFutureWeatherDetailByDate")
           initWeatherData()
           return@withContext futureWeatherDao.getWeatherDetailByDay(date)
       }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather : CurrentWeatherResponseWeatherbit){
        GlobalScope.launch(Dispatchers.IO) {
            Log.d(TAG, "persistFetchedCurrentWeather: " + fetchedWeather.toString())
            currentWeatherDao.upsert(fetchedWeather.data[0])
        }
    }

    private fun persistFetchFutureWeather(fetchedWeather : FutureWeatherResponseWeatherbit){

        fun deleteOldWeatherEntries(){
            val today = org.threeten.bp.LocalDate.now()
            futureWeatherDao.deleteOldEntries(today)
        }

        GlobalScope.launch(Dispatchers.IO) {
            Log.d(TAG, "persistFetchedFutureWeather: " + fetchedWeather.toString())
            deleteOldWeatherEntries()
            val futureWeatherList = fetchedWeather.futureWeatherEntries
            futureWeatherDao.upsert( futureWeatherList)
            cityName = fetchedWeather.cityName
        }
    }

    private suspend fun initWeatherData(){
        Log.d(TAG, "initWeatherData ---- ")

        if(!MainActivity.hasLocationPermission()){
            Log.d(TAG, "initWeatherData - location permission is NOT granted -> waiting")
            return
        }

        val lastCurrentWeatherEntry = currentWeatherDao.getWeatherImperial().value
        if(lastCurrentWeatherEntry == null || locationProvider.hasLocationChanged(lastCurrentWeatherEntry)){
            if(lastCurrentWeatherEntry == null)
                Log.d(TAG, "initWeatherData ---- lastCurrentWeatherEntry")

            if(locationProvider.hasLocationChanged(lastCurrentWeatherEntry))
                Log.d(TAG, "initWeatherData ---- location changed")

            fetchCurrentWeather()
            fetchFutureWeather()
            return;
        }

       if(isFetchCurrentNeed(lastFetchedCurrentWeatherTime)){
           Log.d(TAG, "initWeatherData ---- need to fetch currenty weather data")
           fetchCurrentWeather()
       }else{
           Log.d(TAG, "initWeatherData ---- no need to fetch currenty weather data")
       }

        if(isFetchFutureWeatherNeeded()){
            fetchFutureWeather()
        }
    }

    private suspend fun fetchCurrentWeather(){
        Log.d(TAG, "TT fetchCurrentWeather location: " + locationProvider.getPreferredLocationString() )

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
        Log.d(TAG, "TT fetchCurrentWeather location: " + locationProvider.getPreferredLocationString() )
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