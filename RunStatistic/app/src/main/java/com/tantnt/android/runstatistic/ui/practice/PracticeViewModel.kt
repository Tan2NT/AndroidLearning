package com.tantnt.android.runstatistic.ui.practice

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.PolyUtil
import com.tantnt.android.runstatistic.network.service.GoogleDirectionsNetwork
import com.tantnt.android.runstatistic.utils.MathUtils
import com.tantnt.android.runstatistic.utils.USE_GOOGLE_DIRECTIONS_SERVICE
import com.tantnt.android.runstatistic.utils.nofifyObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.LocalDateTime


/** A method to download json data from url  */

class PracticeViewModel : ViewModel() {

    private val TAG : String = "TDebug"

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    // check if should start the new practice
    private val _shouldStartNewPractice = MutableLiveData<Boolean>()
    val shouldStartNewPractice : LiveData<Boolean>
        get() = _shouldStartNewPractice

    // current day time
    private val _currentTime = MutableLiveData<LocalDateTime>()
    val currentTime : LiveData<LocalDateTime>
        get() = _currentTime

    // total time user has spent for practicing
    private val _totalPracticingTime = MutableLiveData<Int>()
    val totalPracticingTime : LiveData<Int>
        get() = _totalPracticingTime

    // current location of the user
    private val _currentLocation = MutableLiveData<LatLng>()
    val currentLocation : LiveData<LatLng>
        get() = _currentLocation

    // routed line - a collection of points user has walked through
    private val _routedLine = MutableLiveData<ArrayList<LatLng>>()
    val routedLine : LiveData<ArrayList<LatLng>>
        get() = _routedLine

    // bounds of the route
    private val _routeBounds = MutableLiveData<LatLngBounds>()
    val routeBounds : LiveData<LatLngBounds>
        get() = _routeBounds

    // location permission status
    var isLocationGranted : Boolean = false
    fun setLocationPermissionStatus(isGranted: Boolean){
        isLocationGranted = isGranted
    }

    // path direction
    var currentDirectionAngle : Double = 0.0

    // length of the route
    private val _totalDistance = MutableLiveData<Double>()
    val totalDistance : LiveData<Double>
        get() = _totalDistance

    // Coroutine
    private var viewModelJob : Job = Job()
    private val coroutineScope : CoroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
       resetValues()
    }

    private fun resetValues() {
        _routeBounds.value = null
        _routedLine.value = null
        _currentLocation.value = null
        _totalPracticingTime.value = 0
        _totalDistance.value = 0.0
        //_currentTime.value = LocalDateTime.now()
    }

    // the distance at least 3 meters
    private fun isLocationChanged(location: Location): Boolean {
        if (_currentLocation.value == null)
            return true

        val distance = MathUtils.distance(
            location.latitude,
            location.longitude,
            _currentLocation.value!!.latitude,
            _currentLocation.value!!.longitude ) * 1000
        val MIN_DISTANCE = 3
        return (distance >= 3)
    }

    // add new location into the routed
    fun addLocation(location: Location){
        // only execute if user location is changed
        if(isLocationChanged(location)){
            // add new location into the routed
            if(_routedLine.value == null) {
                // start a new path
                val start = LatLng(location.latitude, location.longitude)
                Log.i(TAG, "addLocation add start location")
                _routedLine.value = arrayListOf(start)
                currentDirectionAngle = 0.0
                _routedLine.nofifyObserver()
                _currentLocation.value = start
                _shouldStartNewPractice.value = true
            }
            else {
                val to : LatLng = LatLng(location.latitude, location.longitude)
                if(USE_GOOGLE_DIRECTIONS_SERVICE) {
                    val from : LatLng = _currentLocation.value!!
                    val origin = from.latitude.toString() + "," + from.longitude.toString()
                    val dest = to.latitude.toString() + "," + to.longitude.toString()
                    Log.i(TAG, "find path from " + origin +  " to  " + dest)

                    coroutineScope.launch {
                        // get the directions from current location to new location via Google Directions Service
                        val directionsString = getGoogleDirections(origin, dest)
                        //Log.i(TAG, "respone: " + directionsString)
                        var directionsJson = JSONObject(directionsString)
                        val routes = directionsJson.getJSONArray("routes")
                        if(!routes.isNull(0))
                        {
                            // get bounds
                            val boundJson : JSONObject = routes.getJSONObject(0).getJSONObject("bounds")
                            Log.i(TAG, "bounds: " + boundJson.toString())
                            val bound1 = LatLng(
                                boundJson.getJSONObject("northeast").getDouble("lat"),
                                boundJson.getJSONObject("northeast").getDouble("lng"))
                            val bound2 = LatLng(
                                boundJson.getJSONObject("southwest").getDouble("lat"),
                                boundJson.getJSONObject("southwest").getDouble("lng"))

                            val builder = LatLngBounds.Builder()
                            builder.include(bound1)
                            builder.include(bound2)
                            _routeBounds.value = builder.build()
                            //Log.i(TAG, "routes: " + routes.toString())

                            // parse the routes
                            val firstRoute = routes.get(0) as JSONObject
                            val legs = firstRoute?.getJSONArray("legs")
                            val firstLeg = legs?.let {
                                it[0] as JSONObject
                            }
                            val steps = firstLeg?.getJSONArray("steps")

                            if (steps == null) {
                                Log.i(TAG, " no routed found!")
                            }

                            //for(i in 0 until steps!!.length()) {
                            // get the polyline info
                            val points = steps!!.getJSONObject(0).getJSONObject("polyline").getString("points")
                            val polyPoints = PolyUtil.decode(points)
                            Log.i(TAG, "points: " + points.toString())
                            // add point to the path
                            for(j in 0 until polyPoints.size) {
                                Log.i(TAG, "Add Points " + polyPoints.get(j).toString())
                                _routedLine.value!!.add(polyPoints.get(j))
                            }
                            calculatecurrentDirectionDegrees(to)
                            updateNewLocation(to)
                        }
                    }
                } else {
                    calculatecurrentDirectionDegrees(to)
                    _totalDistance.value = _totalDistance.value!! + MathUtils.distance(
                        to.latitude, to.longitude,
                        _currentLocation.value!!.latitude, _currentLocation.value!!.longitude
                    )
                    updateNewLocation(to)
                }
            }
        }
    }

    private fun calculatecurrentDirectionDegrees(newLocation: LatLng) : Double {
        return MathUtils.angleBetweenTwoPoints(currentLocation.value!!, newLocation)
    }

    private fun updateNewLocation(newLocation : LatLng) {
        _routedLine.value!!.add(newLocation)
        _currentLocation.value = newLocation
        _routedLine.nofifyObserver()
        _shouldStartNewPractice.value = false
    }

    private suspend fun getGoogleDirections(origin : String, dest : String) =
        GoogleDirectionsNetwork.googleDirections.getDirections(origin, dest)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}