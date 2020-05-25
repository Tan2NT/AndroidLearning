package com.tantnt.android.runstatistic.ui.practice

import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tantnt.android.runstatistic.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_practice.*
import kotlin.concurrent.fixedRateTimer

class PracticeFragment : Fragment(), OnMapReadyCallback {

    private val TAG = "TDebug" //PracticeFragment::class.java.simpleName

    // map & location
    private val REQUEST_LOCATION_PERMISSION = 1
    private var mLocationRequest : LocationRequest? = null
    private val LOCATION_UPDATE_INTERVAL = (19 * 1000).toLong()     // 10 seconds
    private val LOCATION_FASTEST_INTERVAL : Long = 2000                    // 2 seconds
    private lateinit var mGoogleMap: GoogleMap
    //private var mapFragment : SupportMapFragment? = null

    private var latitude = 0.0
    private var longitude = 0.0

    private lateinit var practiceViewModel: PracticeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        practiceViewModel =
                ViewModelProviders.of(this).get(PracticeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_practice, container, false)

        val mapFragment  = childFragmentManager?.findFragmentById(R.id.map) as SupportMapFragment

        if(mapFragment != null)
        {
            Log.i(TAG, "onCreateView get Map Async!")
            mapFragment?.getMapAsync(this)
        }

        Log.i(TAG, "onCreateView done!")

        return root
    }

    override fun onMapReady(p0: GoogleMap?) {
        Log.i(TAG, "onMapReady !")
        if (p0 != null) {
            Log.i(TAG, "onMapReady - map is ready!")
            mGoogleMap = p0
            mGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            mGoogleMap!!.addMarker(MarkerOptions().position(LatLng(latitude, longitude)).title(getString(
                            R.string.current_location)))
            startLocationUpdates()
        }
        else Log.w(TAG, "onMapReady - map is  NOT ready!")
    }

    protected fun startLocationUpdates() {
        Log.i(TAG, "start Location update")
        // init location request object
        mLocationRequest = LocationRequest.create()
        mLocationRequest!!.run {
            setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            setInterval(LOCATION_UPDATE_INTERVAL)
            setFastestInterval(LOCATION_FASTEST_INTERVAL)
        }

        // initialize location setting request buulder object
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()

        //initialize location service object
        val settingsClient = activity?.applicationContext?.let {
            LocationServices.getSettingsClient(
                it
            )
        }
        settingsClient!!.checkLocationSettings(locationSettingsRequest)

        // call register location listerner
        registerLocationListener()

    }

    private fun registerLocationListener() {
        // initialize location callback object
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                onLocationChanged(p0!!.lastLocation)
            }
        }

        // 4. add permission if android version is greater than 23
        if(Build.VERSION.SDK_INT >= 23 && checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            activity?.applicationContext?.let { LocationServices.getFusedLocationProviderClient(it).requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper())}
        }
    }

    private fun onLocationChanged(location: Location) {
        // craete message for taost with updated latitude and longitude
        var msg = "Updated Location: " + location.latitude + " , " + location.longitude
        Log.d(TAG, "onLocationChanged " + msg)

        // Show toast message with updated location
        //Toast.makeText()
        val location = LatLng(location.latitude, location.longitude)
        mGoogleMap!!.clear()
        mGoogleMap!!.addMarker(MarkerOptions().position(location).title(getString(R.string.current_location)))
        /*
        *   1: World
            5: Landmass/continent
            10: City
            15: Streets
            20: Buildings
        * */
        val zoomLevel = 18f
        mGoogleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel))
    }

    private fun checkPermission(permission : String): Boolean {
        activity?.applicationContext?.let {
            if (ContextCompat.checkSelfPermission(it, permission) ==  PackageManager.PERMISSION_GRANTED)
                return true
            else {
                requestPermission(permission)
                return false
            }
        }
        return false
    }

    private fun requestPermission(permission : String) {
        activity?.let { ActivityCompat.requestPermissions(it, arrayOf(permission), REQUEST_LOCATION_PERMISSION) }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d(TAG, "onRequestPermissionsResult requestCode: " + requestCode + " - result: " + grantResults[0].toString())
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if(grantResults.contains( PackageManager.PERMISSION_GRANTED)) {
                    Log.i(TAG, "Location permission granted!")
                    mGoogleMap.isMyLocationEnabled = true
                    registerLocationListener()
                }
            }
        }
    }
}
