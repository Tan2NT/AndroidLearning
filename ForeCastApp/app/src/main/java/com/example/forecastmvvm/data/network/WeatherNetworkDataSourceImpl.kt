package com.example.forecastmvvm.data.network

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.forecastmvvm.data.ApixuWeatherApiService
import com.example.forecastmvvm.data.network.response.CurrentWeatherResponseWeatherbit
import com.example.forecastmvvm.internal.NoConnectivityException

class WeatherNetworkDataSourceImpl(
    private val apixuWeatherApiService: ApixuWeatherApiService
) : WeatherNetworkDataSource {
    private val _downloadCurrentWeather = MutableLiveData<CurrentWeatherResponseWeatherbit>()
    override val downloadCurrentWeather: LiveData<CurrentWeatherResponseWeatherbit>
        get() = _downloadCurrentWeather

    override suspend fun fetchCurrentWeather(isUsingDeviceLocation : Boolean, deviceLocation: Location? , location: String, languageCode: String) {
        var fetchCurrentWeather : CurrentWeatherResponseWeatherbit;
        try{
            Log.i("TDebug", "TT fetchCurrentWeather 111" )
            if(isUsingDeviceLocation){
                fetchCurrentWeather = apixuWeatherApiService
                    .getCurrentWeatherOfDeviceLocation("${deviceLocation?.latitude}", "${deviceLocation?.longitude}")
                    .await()
            }
            else{
                fetchCurrentWeather = apixuWeatherApiService
                    .getCurrentWeatherByCityName(location)
                    .await()
            }
            Log.i("TDebug", "TT fetchCurrentWeather receive response" )
            Log.i("TDebug",  fetchCurrentWeather.toString() )
    
            _downloadCurrentWeather.postValue((fetchCurrentWeather))
        }catch (e: NoConnectivityException){
            Log.e("TDebug", "No internet connection. 111" + e.toString())
        }
        catch (e: Exception){
            Log.e("TDebug", "Fetch weather error: " + e.toString())
        }
    }
}