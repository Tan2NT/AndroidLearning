package com.tantnt.android.runstatistic.ui.practice

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.tantnt.android.runstatistic.BuildConfig
import com.tantnt.android.runstatistic.R
import com.tantnt.android.runstatistic.base.ForegroundOnlyLocationService
import com.tantnt.android.runstatistic.utils.*

private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
private const val REQUEST_ENBALE_LOCATION_SETTING = 25

@Suppress("DEPRECATION")
class PracticeFragment : Fragment(), OnMapReadyCallback {

    private val TAG = "TDebug" //PracticeFragment::class.java.simpleName
    
    private lateinit var mGoogleMap: GoogleMap
    private var isMapReady : Boolean = false
    private lateinit var mMarker : Marker

    private lateinit var practiceViewModel: PracticeViewModel

    private var foregroundOnlyLocationServiceBound = false

    // Provides location updates for while -in-use features
    private var foregroundOnlyLocationService: ForegroundOnlyLocationService? = null

    // Listens for location broadcast from ForegroundOnlyLocationService
    private lateinit var foregroundOnlyBroadcastReceiver: ForegroundOnlyBroadcastReceiver

    private lateinit var sharedPreferences: SharedPreferences

    private var locationUpdatesRequested : Boolean = false

    // Monitor connection to the while-in-use service.
    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.i(TAG, "onServiceConnected! + " + name.toString())
            val binder = service as ForegroundOnlyLocationService.LocalBinder
            foregroundOnlyLocationService = binder.service
            foregroundOnlyLocationServiceBound = true

            if(!locationUpdatesRequested){
                if (foregroundPermissionApproved()) {
                    if(locationSettingEnabled()) {
                        foregroundOnlyLocationService?.subscribeToLocationUpdates()
                        locationUpdatesRequested = true
                    }
                    else
                        enableLocationSetting()
                } else {
                    requestForegroundPermissions()
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.i(TAG, "onServiceDisconnected! " + name.toString())
            foregroundOnlyLocationService = null
            foregroundOnlyLocationServiceBound = false
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

        foregroundOnlyBroadcastReceiver = ForegroundOnlyBroadcastReceiver()

        sharedPreferences =
            activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)!!

        Log.i(TAG, "onCreateView done!")
        return root
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart")

        // Bind the foreground service connection
        activity?.let {
            val serviceIntent = Intent(it, ForegroundOnlyLocationService::class.java)
            it.bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(activity?.applicationContext!!).unregisterReceiver(
            foregroundOnlyBroadcastReceiver
        )

        super.onPause()
    }

    override fun onResume() {
        super.onResume()

        // register a broadcast receiver
        LocalBroadcastManager.getInstance(activity?.applicationContext!!).registerReceiver(
            foregroundOnlyBroadcastReceiver,
            IntentFilter(
                ForegroundOnlyLocationService.ACTION_FORGROUND_ONLY_LOCATION_BROADCAST)
        )
    }

    override fun onStop() {
        super.onStop()
        if (foregroundOnlyLocationServiceBound) {
            activity?.let {
                it.unbindService(foregroundOnlyServiceConnection)
            }
            foregroundOnlyLocationServiceBound = false
        }
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
        }
        else Log.w(TAG, "onMapReady - map is  NOT ready!")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d(TAG, "onRequestPermissionsResult requestCode: " + requestCode + " - result: " + grantResults[0].toString())
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE ->  when {
                grantResults.isEmpty() ->
                    Log.d(TAG, "User interaction was cancelled")

                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                {
                    // permission was granted
                    Log.d(TAG, "User interaction permission was granted")
                    //foregroundOnlyLocationService?.subscribeToLocationUpdates()
                    if(locationSettingEnabled()){
                        foregroundOnlyLocationService?.subscribeToLocationUpdates()
                        locationUpdatesRequested = true
                    }
                    else
                        enableLocationSetting()
                }
                else -> {
                    // permission denied.
                    Log.d(TAG, "User interaction permission denied.")
                    Snackbar.make(
                        requireActivity().findViewById(R.id.navigation_practice),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.settings) {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                BuildConfig.APPLICATION_ID,
                                null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        .show()

                }
            }
        }
    }

    // Review Permissions: Method checks if permissions approved.
    private fun foregroundPermissionApproved(): Boolean {
        return PermissionUtils.checkPermissions(
            activity?.applicationContext!!,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
        )
    }

    // TODO: Step 1.0, Review Permissions: Method requests permissions.
    @SuppressLint("ResourceType")
    private fun requestForegroundPermissions() {
        Log.d(TAG, "RequestLocationPermission ---")
        val provideRationale = foregroundPermissionApproved()

        // If the user denied a previous request, but didn't check "Don't ask again", provide
        // additional rationale.
        if (provideRationale) {
            activity?.let {
                Snackbar.make(
                    it.findViewById(R.layout.fragment_practice),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.ok) {
                        // Request permissions
                        requestPermissions(
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
                        )
                    }
                    .show()
            }

        } else {
            Log.d(TAG, "Request foreground only permission")
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    // check location setting
    private fun locationSettingEnabled() : Boolean {
        return LocationUtils.isLocationEnable(requireContext().applicationContext)
    }

    private fun enableLocationSetting() {
        showAlertMessageNoGps()
    }

    private fun showAlertMessageNoGps() {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("This app requires Location setting enabled to perform this feature. Enable Location Setting?")
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_ENBALE_LOCATION_SETTING)
            })
            .setNegativeButton(getString(R.string.no ), { dialog, which ->
                dialog.cancel()
            })
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult requestCode: " + requestCode + " - resultCode: " + resultCode)
        when (requestCode) {
            REQUEST_ENBALE_LOCATION_SETTING -> {
                if(locationSettingEnabled()) {
                    foregroundOnlyLocationService?.subscribeToLocationUpdates()
                    locationUpdatesRequested = true
                }
                else
                    enableLocationSetting()
            }
        }
    }

    /**
     * Receiver for Location broadcasts from [ForegroundOnlyLocationService]
     */
    private inner class ForegroundOnlyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val location = intent?.getParcelableExtra<Location>(
                ForegroundOnlyLocationService.EXTRA_LOCATION
            )

            if(location != null) {
                Log.d(TAG, "ForegroundOnlyBroadcastReceiver add location " + location.toString())
                practiceViewModel!!.addLocation(location)
            }
        }
    }
}

