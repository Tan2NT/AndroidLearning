package com.tantnt.forecast.data.network

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tantnt.forecast.data.ApixuWeatherApiService
import com.tantnt.forecast.data.network.response.CurrentWeatherResponseWeatherbit
import com.tantnt.forecast.data.network.response.FutureWeatherResponseWeatherbit
import com.tantnt.forecast.internal.NoConnectivityException
import com.tantnt.forecast.ui.MainActivity

class WeatherNetworkDataSourceImpl(
    private val apixuWeatherApiService: ApixuWeatherApiService
) : WeatherNetworkDataSource {
    private val TAG : String = "TDebug"
    private val _downloadCurrentWeather = MutableLiveData<CurrentWeatherResponseWeatherbit>()
    override val downloadCurrentWeather: LiveData<CurrentWeatherResponseWeatherbit>
        get() = _downloadCurrentWeather

    private val _downloadFutureWeather = MutableLiveData<FutureWeatherResponseWeatherbit> ()
    override val downloadFutureWeather: LiveData<FutureWeatherResponseWeatherbit>
        get() = _downloadFutureWeather

    // Current weather
    override suspend fun fetchCurrentWeather(isUsingDeviceLocation : Boolean, deviceLocation: Location? , location: String, languageCode: String) {

        Log.d(TAG, "fetchCurrentWeather - WeatherNetworkDataSourceImpl")

        if(!MainActivity.hasLocationPermission()){
            Log.d(TAG, "fetchCurrentWeather - WeatherNetworkDataSourceImpl - location permission did not grant -> waiting")
            return
        }

        var fetchCurrentWeather : CurrentWeatherResponseWeatherbit;
        try{
            Log.d(TAG, "TT fetchCurrentWeather 111" )
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
            Log.d(TAG, "TT fetchCurrentWeather receive response" )
            Log.d(TAG,  fetchCurrentWeather.toString() )
    
            _downloadCurrentWeather.postValue((fetchCurrentWeather))
        }catch (e: NoConnectivityException){
            Log.e("TDebug", "No internet connection. 111" + e.toString())
        }
        catch (e: Exception){
            Log.e("TDebug", "Fetch weather error: " + e.toString())
        }
    }

    // Future weather
    override suspend fun fetchFutureWeather(isUsingDeviceLocation : Boolean, deviceLocation: Location? , location: String, languageCode: String) {
        var fetchFutureWeather : FutureWeatherResponseWeatherbit
        try{
            Log.d(TAG, "TT fetchFutureWeather 111" )
            if(isUsingDeviceLocation){
                fetchFutureWeather = apixuWeatherApiService
                    .getFutureWeatherOfDeviceLocation("${deviceLocation?.latitude}", "${deviceLocation?.longitude}")
                    .await()
            }
            else{
                fetchFutureWeather = apixuWeatherApiService
                    .getFutureWeatherByCityName(location)
                    .await()
            }
            Log.d(TAG, "TT fetchFutureWeather receive response" )
            Log.d(TAG,  fetchFutureWeather.toString() )

            _downloadFutureWeather.postValue((fetchFutureWeather))
        }catch (e: NoConnectivityException){
            Log.e("TDebug", "No internet connection. 111" + e.toString())
        }
        catch (e: Exception){
            Log.e("TDebug", "Fetch weather error: " + e.toString())
        }
    }
}