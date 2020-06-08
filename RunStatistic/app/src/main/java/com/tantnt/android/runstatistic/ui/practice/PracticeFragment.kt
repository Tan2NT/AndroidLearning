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
import androidx.databinding.DataBindingUtil
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
import com.tantnt.android.runstatistic.base.foregroundServiceIsRunning
import com.tantnt.android.runstatistic.base.foregroundServiceSubscribeLocationUpdate
import com.tantnt.android.runstatistic.base.isPracticeRunning
import com.tantnt.android.runstatistic.databinding.FragmentPracticeBinding
import com.tantnt.android.runstatistic.models.PRACTICE_STATUS
import com.tantnt.android.runstatistic.models.PRACTICE_TYPE
import com.tantnt.android.runstatistic.models.PracticeModel
import com.tantnt.android.runstatistic.ui.dialog.SelectingPracticeTypeDialog
import com.tantnt.android.runstatistic.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_practice.*

private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
private const val REQUEST_ENBALE_LOCATION_SETTING = 25
private const val REQUEST_SELECT_PRACTICE_TYPE = 26

@Suppress("DEPRECATION")
class PracticeFragment : Fragment(), OnMapReadyCallback {

    private val TAG = "TDebug" //PracticeFragment::class.java.simpleName
    
    private lateinit var mGoogleMap: GoogleMap
    private var isMapReady : Boolean = false
    private var mMarker : Marker? = null

    private lateinit var practiceViewModel: PracticeViewModel

    private var practiceType = PRACTICE_TYPE.WALKING

    private var foregroundOnlyLocationServiceBound = false

    // Provides location updates for while -in-use features
    private var foregroundOnlyLocationService: ForegroundOnlyLocationService? = null

    // Listens for location broadcast from ForegroundOnlyLocationService
    private lateinit var foregroundOnlyBroadcastReceiver: ForegroundOnlyBroadcastReceiver

    // Monitor connection to the while-in-use service.
    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundOnlyLocationService.LocalBinder
            foregroundOnlyLocationService = binder.service
            foregroundOnlyLocationServiceBound = true

            // check if has location service is running?
            val enabled = foregroundServiceIsRunning && foregroundServiceSubscribeLocationUpdate
                //sharedPreferences.getBoolean(
                //SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false)
            Log.i(TAG, "onServiceConnected! foregroundServiceEnable? " + enabled.toString())

            if (enabled) {
                isPracticeRunning?.let {
                    updateButtonStatus(practiceViewModel.getCurrentPractice())
                }
            } else {
                if (foregroundPermissionApproved()) {
                    if(locationSettingEnabled()) {
                       foregroundOnlyLocationService?.subscribeToLocationUpdates()
                    } else
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
        val activity = requireNotNull(this.activity) {
            "you can only access the viewModel after onActivityCreated"
        }

        // get a reference to the binding object and inflate the fragment views
        val binding: FragmentPracticeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_practice, container, false)

        practiceViewModel =
                ViewModelProviders.of(this, PracticeViewModelFactory(activity.application)).
                    get(PracticeViewModel::class.java)

        binding.setLifecycleOwner(this)
        binding.practiceViewModel = practiceViewModel

        //val root = inflater.inflate(R.layout.fragment_practice, container, false)

        val mapFragment  = childFragmentManager?.findFragmentById(R.id.map) as SupportMapFragment

        if(mapFragment != null)
        {
            Log.i(TAG, "onCreateView get Map Async!")
            mapFragment?.getMapAsync(this)
        }

        practiceViewModel.practice.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (isMapReady && it != null && it.path.size >= 1  && isPracticeRunning){
                    Log.i(TAG, "PracticeFragment - latestPractice: " + it.toString())

                    if(it.path.size == 1) {
                        Log.i(TAG, "PracticeFragment - start Practice")
                        // add the Marker at the starting point
                        val option = MarkerOptions().position(it.path[0]).title(getString(
                            R.string.current_location))
                        option?.icon(BitmapDescriptorFactory.fromResource(R.drawable.orange))
                        mGoogleMap!!.addMarker(option)
                        moveCameraWithZoom(it.path[0], 16.0f)
                    }

                    // adding path
                    addPath(it.path)

                    // update the UI

                    // update button status
                    updateButtonStatus(it)
                }
            }
        })

        foregroundOnlyBroadcastReceiver = ForegroundOnlyBroadcastReceiver()

        Log.i(TAG, "onCreateView done!")
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // register button listener
        // start a practice
        start_practice_btn.setOnClickListener {
            if(!isPracticeRunning) {
                // we must select practice before start a practice
                openSelectPracticeTypeDialog()
            }
            else {
                // user resume a practice
                foregroundOnlyLocationService?.resumePractice()
                stop_practice_btn.visibility = View.VISIBLE
                pause_practice_btn.visibility = View.VISIBLE
                start_practice_btn.visibility = View.GONE
            }
        }

        // stop the practice
        stop_practice_btn.setOnClickListener {
            foregroundOnlyLocationService?.stopPractice()

           onPracticeStopped()
        }

        // pause the practice
        pause_practice_btn.setOnClickListener {
            foregroundOnlyLocationService?.pausePractice()

            stop_practice_btn.visibility = View.VISIBLE
            pause_practice_btn.visibility = View.GONE
            start_practice_btn.visibility = View.VISIBLE
        }

        initButtonStatus()
    }

    fun openSelectPracticeTypeDialog() {
        val ft = fragmentManager?.beginTransaction()
        val selectPracticeTypeDialog = SelectingPracticeTypeDialog()
        selectPracticeTypeDialog.setTargetFragment(this, REQUEST_SELECT_PRACTICE_TYPE)
        selectPracticeTypeDialog.show(ft!!, "PracticeTypeDialog")
    }

    private fun initButtonStatus() {
        stop_practice_btn.visibility = View.GONE
        pause_practice_btn.visibility = View.GONE
        start_practice_btn.visibility = View.VISIBLE
    }

    private fun updateButtonStatus(practiceModel : PracticeModel?) {
        if (practiceModel != null)
        {
            Log.i(LOG_TAG, "updateStatusButton has practice")
            when(practiceModel.status) {
                PRACTICE_STATUS.RUNNING -> {
                    stop_practice_btn.visibility = View.VISIBLE
                    pause_practice_btn.visibility = View.VISIBLE
                    start_practice_btn.visibility = View.GONE
                }

                PRACTICE_STATUS.PAUSING -> {
                    stop_practice_btn.visibility = View.VISIBLE
                    pause_practice_btn.visibility = View.GONE
                    start_practice_btn.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun onPracticeStopped() {
        start_practice_btn.visibility = View.VISIBLE
        stop_practice_btn.visibility = View.GONE
        pause_practice_btn.visibility = View.GONE
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
        Log.i(TAG, "onPause")
        LocalBroadcastManager.getInstance(activity?.applicationContext!!).unregisterReceiver(
            foregroundOnlyBroadcastReceiver
        )

        super.onPause()
    }

    override fun onResume() {
        Log.i(TAG, "onResume")
        super.onResume()

        // register a broadcast receiver
        LocalBroadcastManager.getInstance(activity?.applicationContext!!).registerReceiver(
            foregroundOnlyBroadcastReceiver,
            IntentFilter(
                ForegroundOnlyLocationService.ACTION_FORGROUND_ONLY_LOCATION_BROADCAST)
        )
    }

    override fun onStop() {
        Log.d(TAG, "onStop() ---- ")
        super.onStop()
        if (foregroundOnlyLocationServiceBound) {
            activity?.let {
                it.unbindService(foregroundOnlyServiceConnection)
            }
            foregroundOnlyLocationServiceBound = false
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy() ---")
        super.onDestroy()
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

    private fun addPath(path : ArrayList<LatLng>) {
        Log.i(TAG, "addMapDirections !")
        var polyLineOptions : PolylineOptions = PolylineOptions()
        polyLineOptions.color(Color.RED)
        polyLineOptions.width(20f)

        for(i in 0 until path.size){
            Log.i(TAG, path.get(i).toString())
            polyLineOptions.add(path.get(i))
        }

        if(mMarker == null ) {
            val option = MarkerOptions().position(path.get(path.size - 1)).title(getString(
                R.string.current_location))
            option?.icon(BitmapDescriptorFactory.fromResource(R.drawable.green))
            mMarker = mGoogleMap!!.addMarker(option)
        } else
            mMarker?.position = path.get(path.size - 1)
        moveCameraWithZoom(path.get(path.size - 1), 17.0f)
        mGoogleMap.addPolyline(polyLineOptions)

//        Adding bounds
//        practiceViewModel.routeBounds.value?.let {
//            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(practiceViewModel.routeBounds.value, 70))
//        }
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
                }
                else
                    enableLocationSetting()
            }
            REQUEST_SELECT_PRACTICE_TYPE -> {
                val type = data?.getIntExtra("practice_type", 0)!!
                practiceType = PRACTICE_TYPE.values() [type]
                foregroundOnlyLocationService?.startPractice(practiceType)
                Log.d(TAG, "onActivityResult practice_type: $type")
            }
        }
    }

    /**
     * Receiver for Location broadcasts from [ForegroundOnlyLocationService]
     */
    private inner class ForegroundOnlyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val location = intent?.getParcelableExtra<Location>(
                ForegroundOnlyLocationService.EXTRA_LOCATION)
            if(isMapReady && location != null) {
                val point = LatLng(location!!.latitude, location!!.longitude)
                val option = MarkerOptions().position(point).title(getString(
                    R.string.current_location))
                option?.icon(BitmapDescriptorFactory.fromResource(R.drawable.green))
                mMarker = mGoogleMap!!.addMarker(option)
                moveCameraWithZoom(point, 16.0f)
            }

            val isPracticeStopped = intent?.getBooleanExtra(ForegroundOnlyLocationService.EXTRA_PRACTICE_STOPPED, false)
            if (isPracticeStopped == true){
                onPracticeStopped()
            }

        }
    }
}

