package com.tantnt.forecast.data.provider

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.tantnt.forecast.data.db.unitlocalized.current.ImperialCurrentWeatherEntry
import com.tantnt.forecast.internal.LocationPermissionNotGranted
import com.tantnt.forecast.internal.asDeferred
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred

const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"

class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context) : PreferencesProvider(context), LocationProvider {

    private val appContext = context.applicationContext

    private val TAG : String = "TDebug"

    override suspend fun hasLocationChanged(lastImperialCurrentWeatherEntry: ImperialCurrentWeatherEntry?): Boolean {

        val deviceLocationChanged = try {
            hasDeviceLocationChanged(lastImperialCurrentWeatherEntry)
        }catch (e: LocationPermissionNotGranted){
            false
        }

        return deviceLocationChanged || hasCustomLocationChanged(lastImperialCurrentWeatherEntry)
    }

    override suspend fun getPreferredLocationString(): String {
        if(isUsingDeviceLocation()){
            try {
                val deviceLocation = getLastDeviceLocation().await()
                    ?: return "${getCustomLocationName()}"
                return "${deviceLocation.latitude}, ${deviceLocation.longitude}"
            } catch (e: LocationPermissionNotGranted){
                return "${getCustomLocationName()}"
            }
        }
        else
        {
            return "${getCustomLocationName()}"
        }
    }
    override suspend fun isUsingDeviceLocation() : Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    override suspend fun getDeviceLocation(): Location? {
        val deviceLocation = getLastDeviceLocation().await()
        return deviceLocation
    }

    private suspend fun hasDeviceLocationChanged(lastImperialCurrentWeatherEntry: ImperialCurrentWeatherEntry?) : Boolean {
        if(!isUsingDeviceLocation())
            return false

        val deviceLocation = getLastDeviceLocation().await()
            ?: return false

        // comparing double can not be done with "=="
        val comparisonThreshold = 0.03

        if(lastImperialCurrentWeatherEntry == null) {
            Log.d(TAG, "hasDevicLocationChanged - last current location is null")
            return true
        }

        return Math.abs(deviceLocation.latitude - lastImperialCurrentWeatherEntry?.lat!!.toDouble()) > comparisonThreshold &&
                Math.abs(deviceLocation.longitude - lastImperialCurrentWeatherEntry?.lon!!.toDouble()) > comparisonThreshold
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location?>{
        return if(hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw LocationPermissionNotGranted()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(appContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private suspend fun hasCustomLocationChanged(lastImperialCurrentWeatherEntry: ImperialCurrentWeatherEntry?) : Boolean {
        if(!isUsingDeviceLocation()){
            val customLocationName = getCustomLocationName()
            return customLocationName != lastImperialCurrentWeatherEntry?.cityName
        }else{
            return false;
        }
    }

    private fun getCustomLocationName() : String? {
        return preferences.getString(CUSTOM_LOCATION, null)
    }
}