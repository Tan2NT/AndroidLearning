package com.tantnt.android.runstatistic.ui.practice

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.tantnt.android.runstatistic.R
import com.tantnt.android.runstatistic.utils.LifecycleBoundLocationManager
import com.tantnt.android.runstatistic.utils.LocationUtils
import com.tantnt.android.runstatistic.utils.PermissionUtils

@Suppress("DEPRECATION")
class PracticeFragment : Fragment(), OnMapReadyCallback {

    private val TAG = "TDebug" //PracticeFragment::class.java.simpleName

    // map & location
    private val REQUEST_LOCATION_PERMISSION = 1
    private var mLocationRequest : LocationRequest? = null
    private val LOCATION_UPDATE_INTERVAL = (2 * 1000).toLong()     // 10 seconds
    private val LOCATION_FASTEST_INTERVAL : Long = 2000                    // 2 seconds
    private lateinit var mGoogleMap: GoogleMap
    private var isMapReady : Boolean = false

    private var latitude = 0.0
    private var longitude = 0.0
    private lateinit var mMarker : Marker

    private lateinit var practiceViewModel: PracticeViewModel

    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    // Location callback when ever location is changed
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            practiceViewModel.addLocation(result!!.lastLocation)
        }
    }

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

        practiceViewModel.shouldStartNewPractice.observe(viewLifecycleOwner, Observer {
            if (it == true){
                Log.i(TAG, "update the marker")
                // add the Marker at the starting point
                val option = MarkerOptions().position(practiceViewModel.currentLocation.value!!).title(getString(
                    R.string.current_location))
                option?.icon(BitmapDescriptorFactory.fromResource(R.drawable.direction_blue))
                mMarker = mGoogleMap!!.addMarker(option)

                moveCameraWithZoom(practiceViewModel.currentLocation.value!!, 17.0f)

            }
        })

        practiceViewModel.routedLine.observe(viewLifecycleOwner, Observer {
            if(practiceViewModel.isLocationGranted && isMapReady ) {
                // update the routes
                if(practiceViewModel.routedLine.value != null && practiceViewModel.routedLine.value!!.size > 1)
                    addMapDirections()
            }
        })

        fusedLocationProviderClient = activity?.let {
            LocationServices.getFusedLocationProviderClient(
                it
            )
        }!!

        startLocationUpdates()

        Log.i(TAG, "onCreateView done!")
        return root
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun moveCameraWithZoom(target: LatLng, zoomLevel: Float) {
        /*
         1: World
        5: Landmass/continent
        10: City
        15: Streets
        20: Buildings
        * */
        mGoogleMap!!.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                target,
                zoomLevel
            )
        )
    }

    private fun addMapDirections() {
        Log.i(TAG, "addMapDirections !")
        var polyLineOptions : PolylineOptions = PolylineOptions()
        polyLineOptions.color(Color.RED)
        polyLineOptions.width(20f)

        val routes : ArrayList<LatLng> = practiceViewModel.routedLine.value!!
        for(i in 0 until routes!!.size){
            val point : LatLng = routes!!.get(i)
            Log.i(TAG, "addMapDirections add point Lat: " + point.latitude + " - Lon:" + point.longitude)
            polyLineOptions.add(routes!!.get(i))
        }
//        polyLineOptions.add(LatLng(85.0, 5.0))
//        .add(LatLng(-85.0, 5.0))
//        mGoogleMap!!.addMarker(MarkerOptions().position(routes.get(routes.size - 1)).title(getString(
//            R.string.current_location)))
        mMarker?.position = practiceViewModel.currentLocation.value!!
        mMarker?.rotation = practiceViewModel.currentDirectionAngle.toFloat()
        mGoogleMap.addPolyline(polyLineOptions)
        practiceViewModel.routeBounds.value?.let {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(practiceViewModel.routeBounds.value, 70))
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        if (p0 != null) {
            Log.i(TAG, "onMapReady - map is ready!")
            mGoogleMap = p0
            isMapReady = true
            mGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            //startLocationUpdates()
        }
        else Log.w(TAG, "onMapReady - map is  NOT ready!")
    }

    protected fun startLocationUpdates() {
        Log.i(TAG, "start Location update")
        var isPermissionEnable = true

        activity?.let { myActivity ->
                            activity?.applicationContext?.let { appContext ->
                                isPermissionEnable = PermissionUtils.checkPermissions(
                                    appContext,
                                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    )
                                if(!isPermissionEnable)
                                    requestPermissions(
                                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                                            android.Manifest.permission.ACCESS_COARSE_LOCATION),
                                        REQUEST_LOCATION_PERMISSION
                                    )
                }
            }!!

        if (!isPermissionEnable)
            return

        practiceViewModel.isLocationGranted = true

        activity?.applicationContext?.let {
            Log.i(TAG, "startLocationUpdates enable location setting")
            if(!LocationUtils.isLocationEnable(it)){
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }else {
            }
        }

        Log.i(TAG, "startLocationUpdates bindLoctionManager")
        bindLocationManager()
    }

    private fun bindLocationManager() {
        LifecycleBoundLocationManager(this, fusedLocationProviderClient, mLocationCallback )
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
                    practiceViewModel.isLocationGranted = true
                    startLocationUpdates()
                }
            }
        }
    }
}

