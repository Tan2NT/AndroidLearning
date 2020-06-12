package com.tantnt.android.runstatistic.ui.practice

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.*
import android.content.pm.PackageManager
import android.graphics.*
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.BuildConfig
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.ActivityRecognitionClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.tantnt.android.runstatistic.R
import com.tantnt.android.runstatistic.base.*
import com.tantnt.android.runstatistic.databinding.FragmentPracticeBinding
import com.tantnt.android.runstatistic.models.PRACTICE_STATUS
import com.tantnt.android.runstatistic.models.PRACTICE_TYPE
import com.tantnt.android.runstatistic.models.PracticeModel
import com.tantnt.android.runstatistic.ui.dialog.SelectingPracticeTypeDialog
import com.tantnt.android.runstatistic.utils.*
import kotlinx.android.synthetic.main.confirm_popup_layout.view.*
import kotlinx.android.synthetic.main.fragment_practice.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception

private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
private const val REQUEST_ENBALE_LOCATION_SETTING = 25
private const val REQUEST_SELECT_PRACTICE_TYPE = 26
private const val REQUEST_DETECT_ACTIVITY_REQUEST_CODE = 27

private const val ZOOM_LEVEL_SMALL = 16.0f
private const val ZOOM_LEVEL_MEDIUM = 17.0f
private const val ZOOM_LEVEL_DETAIL = 18.0f


@Suppress("DEPRECATION")
class PracticeFragment : Fragment(), OnMapReadyCallback {

    private val TAG = "TDebug" //PracticeFragment::class.java.simpleName
    
    private lateinit var mGoogleMap: GoogleMap
    private var isMapReady : Boolean = false
    private var mCurrentMarker : Marker? = null
    private var mStartMarker : Marker? = null
    private var mPolyline: Polyline? = null

    private lateinit var practiceViewModel: PracticeViewModel

    private var practiceType = PRACTICE_TYPE.WALKING

    /**
     * The entry point for interacting with activity recognition.
     */
    private lateinit var activityRecognitionClient : ActivityRecognitionClient

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

        val mapFragment  = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        if(mapFragment != null)
        {
            Log.i(TAG, "onCreateView get Map Async!")
            mapFragment.getMapAsync(this)
        }

        practiceViewModel.practice.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (isMapReady && it.path.size >= 1  && isPracticeRunning){

                    // adding path
                    addPath(it.path)

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
                mPolyline = null
                mCurrentMarker = null
                mStartMarker = null
                mGoogleMap.clear()
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
            showAlertConfirmStopPractice()
        }

        // pause the practice
        pause_practice_btn.setOnClickListener {
            foregroundOnlyLocationService?.pausePractice()

            stop_practice_btn.visibility = View.VISIBLE
            pause_practice_btn.visibility = View.GONE
            start_practice_btn.visibility = View.VISIBLE
        }

        initButtonStatus()

        activityRecognitionClient = ActivityRecognitionClient(requireActivity())
    }

    fun openSelectPracticeTypeDialog() {
        val ft = fragmentManager?.beginTransaction()
        val selectPracticeTypeDialog = SelectingPracticeTypeDialog()
        selectPracticeTypeDialog.setTargetFragment(this, REQUEST_SELECT_PRACTICE_TYPE)
        ft?.let {
            selectPracticeTypeDialog.show(ft, "PracticeTypeDialog")
        }
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
                PRACTICE_STATUS.ACTIVE, PRACTICE_STATUS.NOT_ACTIVE -> {
                    stop_practice_btn.visibility = View.VISIBLE
                    pause_practice_btn.visibility = View.VISIBLE
                    start_practice_btn.visibility = View.GONE
                }

                PRACTICE_STATUS.PAUSING -> {
                    stop_practice_btn.visibility = View.VISIBLE
                    pause_practice_btn.visibility = View.GONE
                    start_practice_btn.visibility = View.VISIBLE
                }

                PRACTICE_STATUS.COMPETED -> {
                    initButtonStatus()
                }
            }
        }
    }

    private fun onPracticeStopped() {
        Log.i(LOG_TAG, "onPracticeStopped - ")

        // ensure map is switch zoom level
        mGoogleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                practiceViewModel.practice.value?.path?.get(0),
                ZOOM_LEVEL_MEDIUM
            )
        )

         runBlocking {
             val job = launch (context = Dispatchers.Default) {
                 delay(100)
             }
             job.join()
         }

        start_practice_btn.visibility = View.VISIBLE
        stop_practice_btn.visibility = View.GONE
        pause_practice_btn.visibility = View.GONE

        removeActivityRecognitionUpdates()
    }

    fun captureMapScreen(practive: PracticeModel, map : GoogleMap, context: Context){

            var bitmap : Bitmap? = null
            val callback =
                GoogleMap.SnapshotReadyCallback { snapshot ->
                    // TODO Auto-generated method stub
                    bitmap = snapshot

                    bitmap?.let {

                        try {
                            // Share result to others
                            val bitmapPath: String =
                                MediaStore.Images.Media.insertImage(
                                    context.contentResolver,
                                    bitmap,
                                    practive.startTime.toString(),
                                    null
                                )

                            bitmapPath?.let {
                                val actionResult
                                        = PracticeFragmentDirections.actionNavigationPracticeToResultFragment(
                                    bitmapPath,
                                    practive.practiceType.value,
                                    practive.distance.toFloat(),
                                    practive.duration,
                                    practive.speed.toFloat(),
                                    practive.calo.toFloat())
                                findNavController().navigate(actionResult)
                            }
                        } catch (e: Exception) {
                            Log.i(LOG_TAG, "Failed to save practice image into MediaStore $e")
                        }
                    }
                }
            map.snapshot(callback)
        }

    private fun openResultScreen() {
        Log.i(LOG_TAG, "openResultScreen - 111 ")
        captureMapScreen(practiceViewModel.practice.value!!, mGoogleMap, requireContext())
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
        foregroundOnlyLocationService?.stopPractice()
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
        mGoogleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                target,
                zoomLevel
            )
        )
    }

    private fun addPath(path : ArrayList<LatLng>) {
        var polyLineOptions : PolylineOptions = PolylineOptions()
        polyLineOptions.color(Color.RED)
        polyLineOptions.width(20f)

        for(i in 0 until path.size){
            Log.i(TAG, path.get(i).toString())
            polyLineOptions.add(path.get(i))
        }

        if(mCurrentMarker == null ) {
            val option = MarkerOptions().position(path.get(path.size - 1)).title(getString(
                R.string.current_location))
            option?.icon(BitmapDescriptorFactory.fromResource(R.drawable.green_circle))
            mCurrentMarker = mGoogleMap.addMarker(option)
        } else
            mCurrentMarker?.position = path.get(path.size - 1)
        moveCameraWithZoom(path.get(path.size - 1), ZOOM_LEVEL_DETAIL)

        mPolyline = mGoogleMap.addPolyline(polyLineOptions)

        if(path.size == 1){
            val option = MarkerOptions().position(path.get(0)).title(getString(
                R.string.current_location))
            option?.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_flag))
            mStartMarker = mGoogleMap.addMarker(option)
        }

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
                    it.findViewById(R.id.navigation_practice),
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
        builder.setMessage(getString(R.string.location_setting_required))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes), DialogInterface.OnClickListener { dialog, which ->
                startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_ENBALE_LOCATION_SETTING)
            })
            .setNegativeButton(getString(R.string.no ), { dialog, which ->
                dialog.cancel()
            })
        builder.show()
    }

    private fun showAlertConfirmStopPractice() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.confirm_popup_layout,null)
        dialogView.message_text.text = getString(R.string.confirm_stop_practice)
        val builder = AlertDialog.Builder(context)
        builder.setView(dialogView)

        val alertDialog = builder.show()

        dialogView.btn_ok.setOnClickListener {
            onPracticeStopped()
            openResultScreen()
            foregroundOnlyLocationService?.stopPractice()
            alertDialog.dismiss()
        }

        dialogView.btn_cancel.setOnClickListener {
            alertDialog.dismiss()
        }

        val window = alertDialog.window
        val wlp = window?.attributes
        wlp?.gravity = Gravity.BOTTOM
        window?.attributes = wlp

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
                registerActivityRecognitionUpdates()
                stop_practice_btn.visibility = View.VISIBLE
                pause_practice_btn.visibility = View.VISIBLE
                start_practice_btn.visibility = View.GONE
                Log.d(TAG, "onActivityResult practice_type: $type")
            }
        }
    }

    /**
     * Register for activity recognition updates
     * Registers success and failure callbacks.
     */
    fun registerActivityRecognitionUpdates() {
        Log.i(TAG, "registerActivityRecognitionUpdates")

        val task = activityRecognitionClient.requestActivityUpdates(
            DETECTION_INTERVAL_IN_MILLISECONDS,
            getActivityDetectionPendingIntent()
        )

        task.addOnSuccessListener {
            Log.i(TAG, "addOnSuccessListener Activity updates enabled!")
        }

        task.addOnFailureListener {
            Log.e(TAG, "addOnFailureListener faile to enable activity updates! err: ${it.toString()}")
        }
    }

    fun getActivityDetectionPendingIntent(): PendingIntent {
        val intent = Intent(requireContext(), DetectedActivitiesIntentService::class.java)

        // use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // requestActivityUpdates() and removeActivityUpdates()
        return PendingIntent.getService(requireContext(), REQUEST_DETECT_ACTIVITY_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    /**
     * Removes activity recognition updates
     * Register success and failure callbacks
     */
    fun removeActivityRecognitionUpdates() {

        Log.i(TAG, "removeActivityRecognitionUpdates")

        val task = activityRecognitionClient.removeActivityUpdates(
            getActivityDetectionPendingIntent()
        )

        task.addOnSuccessListener {
            Log.i(TAG, "addOnSuccessListener Activity  recognition updates removed!")
        }

        task.addOnFailureListener {
            Log.i(TAG, "addOnSuccessListener fail to remove Activity recognition updates")
        }
    }


    /**
     * Receiver for Location broadcasts from [ForegroundOnlyLocationService]
     */
    private inner class ForegroundOnlyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val location = intent?.getParcelableExtra<Location>(
                ForegroundOnlyLocationService.EXTRA_LOCATION)
            if(isMapReady && location != null && !isPracticeRunning) {
                val point = LatLng(location.latitude, location.longitude)
                val option = MarkerOptions().position(point).title(getString(
                    R.string.current_location))
                option?.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_flag))
                if (mStartMarker == null) {
                    mStartMarker = mGoogleMap.addMarker(option)
                    moveCameraWithZoom(point, ZOOM_LEVEL_MEDIUM)
                }
            }

            val isPracticeStopped = intent?.getBooleanExtra(ForegroundOnlyLocationService.EXTRA_PRACTICE_STOPPED, false)
            if (isPracticeStopped == true){
                onPracticeStopped()
            }

        }
    }
}

