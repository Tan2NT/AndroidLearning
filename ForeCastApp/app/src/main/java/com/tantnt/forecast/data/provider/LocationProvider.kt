package com.tantnt.forecast.data.provider
import android.location.Location
import com.tantnt.forecast.data.db.unitlocalized.current.ImperialCurrentWeatherEntry

interface LocationProvider {
    suspend fun hasLocationChanged(imperialCurrentWeatherEntry: ImperialCurrentWeatherEntry?) : Boolean
    suspend fun getPreferredLocationString() : String
    suspend fun isUsingDeviceLocation() : Boolean
    suspend fun getDeviceLocation() : Location?
}