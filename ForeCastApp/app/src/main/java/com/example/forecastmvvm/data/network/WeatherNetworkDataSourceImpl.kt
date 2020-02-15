package com.example.forecastmvvm.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.forecastmvvm.data.ApixuWeatherApiService
import com.example.forecastmvvm.data.network.response.CurrentWeatherResponse
import com.example.forecastmvvm.internal.NoConnectivityException

class WeatherNetworkDataSourceImpl(
    private val apixuWeatherApiService: ApixuWeatherApiService
) : WeatherNetworkDataSource {
    private val _downloadCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadCurrentWeather

    override suspend fun fetchCurrentWeather(location: String, languageCode: String) {
        try{
            Log.i("TDebug", "TT fetchCurrentWeather 111" )
            val fetchCurrentWeather = apixuWeatherApiService
                .getCurrentWeather(location, languageCode)
                .await()

            Log.i("TDebug", "TT currentWeatherEntry 111: " + fetchCurrentWeather.currentWeatherEntry.toString() )

            _downloadCurrentWeather.postValue((fetchCurrentWeather))
        }catch (e: NoConnectivityException){
            Log.e("TDebug", "No internet connection. 111", e)
        }
    }
}