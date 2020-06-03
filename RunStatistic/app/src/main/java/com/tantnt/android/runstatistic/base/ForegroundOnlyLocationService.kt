package com.tantnt.android.runstatistic.base

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.tantnt.android.runstatistic.MainActivity
import com.tantnt.android.runstatistic.R
import com.tantnt.android.runstatistic.database.getDatabase
import com.tantnt.android.runstatistic.models.PracticeModel
import com.tantnt.android.runstatistic.models.asDatabasePractice
import com.tantnt.android.runstatistic.repository.RunstatisticRepository
import com.tantnt.android.runstatistic.utils.*
import com.tantnt.android.runstatistic.utils.SharedPreferenceUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.toString

/**
 * Service tracks location when requested and updates Activity via binding.
 * If Activity is stopped/ unbinds and tracking is enabled, the service promotes
 * itself to a foreground service to insure location updates interrupted
 */

var foregroundServiceIsRunning = false
var foregroundServiceSubscribeLocationUpdate = false
var  isPracticeRunning = false
const val PRACTICE_UPDATE_INTERVAL = 2   // seconds

class ForegroundOnlyLocationService  : Service() {

    /**
     * Store the information of this practice
     */
    private var lastUpdateTime : Long = System.currentTimeMillis()

    private var currentPractice : PracticeModel = PracticeModel(
        start_time = TimeUtils.getTimeInMilisecond(),
        duration = 0,
        distance = 0.0,
        calo = 0.0,
        speed = 0.0,
        status = PRACTICE_STATUS.NOT_RUNNING.value,
        path = arrayListOf()
    )

    // Coroutines scope to allow repository to safe access the database
    private lateinit var coroutineScope : CoroutineScope

    private lateinit var repository: RunstatisticRepository

    /**
     * checks whether the bound activity has really gone away (foreground service with notification
     * created) or simply orientation changed
     */

    private var configurationChange = false

    private var serviceRunningInForeground = false

    private var localBinder = LocalBinder()

    inner class LocalBinder() : Binder() {
        internal val service: ForegroundOnlyLocationService
            get() = this@ForegroundOnlyLocationService
    }

    private lateinit var notificationManager: NotificationManager

    // for receiving location update
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // Requirements for the location updates
    private lateinit var locationRequest: LocationRequest

    // called when FusedLocationProviderClient has a new location
    private lateinit var locationCallback: LocationCallback

    // current location
    private var currentLocation: Location? = null

    override fun onCreate() {
        Log.d(TAG, "ForegroundService onCreate() ---- ")

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Review the FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Create a LocationRequest
        locationRequest = LocationRequest().apply {
            // how often the location is updated
            interval = TimeUnit.SECONDS.toMillis(3)

            // your application will never receive updates more frequently than this value
            fastestInterval = TimeUnit.SECONDS.toMillis(1)

            // sets the maximum time when batched location updates are delivered. Updates may be
            // delivered sooner than this interval
            maxWaitTime = TimeUnit.SECONDS.toMillis(10)

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        repository = RunstatisticRepository(getDatabase(application))
        coroutineScope = CoroutineScope(Dispatchers.Default)

        // Initialize the LocationCallback
        locationCallback = object: LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                Log.d(TAG, "onLocationResult lastLocation:  " + p0?.lastLocation.toString())

                if(p0?.lastLocation != null ) {

                   if(isPracticeRunning){
                       onNewLocation(p0.lastLocation)
                   }else {
                       currentLocation = p0?.lastLocation
                       val intent = Intent(ACTION_FORGROUND_ONLY_LOCATION_BROADCAST)
                       intent.putExtra(EXTRA_LOCATION, currentLocation)
                       LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                   }
                }
            }
        }
        foregroundServiceIsRunning = true
    }

    fun startPractice() {
        if(currentPractice.status == PRACTICE_STATUS.PAUSING.value) {
            resumePractice()
        }
        else {
            isPracticeRunning = true
            currentLocation = null
            currentPractice = PracticeModel(
                start_time = TimeUtils.getTimeInMilisecond(),
                duration = 0,
                distance = 0.0,
                calo = 0.0,
                speed = 0.0,
                status = PRACTICE_STATUS.RUNNING.value,
                path = arrayListOf())
            if (currentLocation != null) {
                currentPractice.path.add(
                    LatLng(
                        currentLocation!!.latitude,
                        currentLocation!!.longitude
                    )
                )
                savePractice()
            }
        }
    }

    fun stopPractice() {
        currentPractice.status = PRACTICE_STATUS.COMPETED.value
        isPracticeRunning = false
        savePractice()
        unsubscribeToLocationUpdates()
        stopSelf()
    }

    fun pausePractice() {
        currentPractice.status = PRACTICE_STATUS.PAUSING.value
        savePractice()
    }

    fun resumePractice() {
        currentPractice.status = PRACTICE_STATUS.RUNNING.value
        lastUpdateTime = TimeUtils.getTimeInMilisecond()
    }

    fun savePractice() {
        coroutineScope.launch {
            try {
                Log.d(TAG, "try to insert a practice into database " + currentPractice.toString())
                repository.insertPractice(currentPractice.asDatabasePractice())
            } catch (e: Exception) {
                Log.e(TAG, "insert to database failed : " + e.toString())
            }
        }
    }

    private fun onNewLocation(location: Location?) {
        Log.d(LOG_TAG, "onNewLocation practiceStarted: " + isPracticeRunning.toString())
//        if(!practiceStarted || currentPractice.status != PRACTICE_STATUS.RUNNING.value)
////            return

        // check the location
        if(currentPractice.path.size == 0) {
            // Start a new practice
            currentLocation = location
            currentPractice.path.add(LatLng(location!!.latitude, location!!.longitude))
            currentPractice.start_time = System.currentTimeMillis()
            savePractice()
            updateForegroundNotificationifNeed()
            return
        }

        val distance = MathUtils.distance(
            currentLocation!!.latitude,
            currentLocation!!.longitude,
            location!!.latitude,
            location!!.longitude)

        // only allow to add new location if it's not too close with the previous location
        if(distance >= 0.000) {
            // update the practice
            currentPractice.path.add(LatLng(location!!.latitude, location!!.longitude))
            currentPractice.distance += distance
            currentPractice.duration += TimeUtils.getElapseTimeFrom(lastUpdateTime)
            lastUpdateTime = TimeUtils.getTimeInMilisecond()
            currentPractice.speed = (currentPractice.distance/(currentPractice.duration / 3600) )   // Km/h
            currentPractice.status = PRACTICE_STATUS.RUNNING.value
            currentPractice.calo = (currentPractice.duration * KcalCaclator.burnedByyWalkingPerMinute(
                USER_WEIGHT_DEFAULT, currentPractice.speed, USER_HEIGHT_DEFAULT)
                    ).aroundPlace(2)

            // save updated practice into the database
            savePractice()
        }

        // update current location
        currentLocation!!.latitude = location!!.latitude
        currentLocation!!.longitude = location!!.longitude
        updateForegroundNotificationifNeed()
    }

    private fun updateForegroundNotificationifNeed(){
        // @Todo
        // Updates notification content if this service is running as a foreground service
        if(serviceRunningInForeground) {
            notificationManager.notify(
                NOTIFICATION_ID,
                generateNotification(currentLocation))
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand() ---- ")

        val cancelLocationTrackingFromNotification =
            intent?.getBooleanExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, false)

        if (cancelLocationTrackingFromNotification!!) {
            unsubscribeToLocationUpdates()
            stopSelf()
        }

        // Tells the system not to recreate the service after it's been killed
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "onBind() --- ")

        // MainActivity (client) comes into forground and binds service, so the service can become a
        // background service
        stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
        return localBinder
    }

    override fun onRebind(intent: Intent?) {
        Log.d(TAG, "onRebind -----")

        // MainActivity (client) return to the foreground and rebinds to service, so the service
        // can become a background service
        stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind ---")

        // MainActivity (client) leaves foreground, so service needs to become a foreground service
        // to update the practicing info
        // NOTE: If this method is called due to a configuration change in MainActivity, we do nothing

        if(!configurationChange && SharedPreferenceUtil.getLocationTrackingPref(this)) {
            Log.d(TAG, "Start foreground service")
            val notification = generateNotification(currentLocation)
            startForeground(NOTIFICATION_ID, notification)
            serviceRunningInForeground = true
        }

        // ensure onREbind() is call if MainActivity (client) rebinds.
        return true
    }

    override fun onDestroy() {
        Log.d(TAG, "foregroundService onDestroy()")
        foregroundServiceIsRunning = false
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configurationChange = true
    }

    fun subscribeToLocationUpdates() {
        Log.d(TAG, "subscribeToLocationUpdates()")

        SharedPreferenceUtil.saveLocationTrackingPref(this, true)
        foregroundServiceSubscribeLocationUpdate = true

        // Binding to this service doesn't actually trigger onStartCommand(). That is needed to
        // ensure this Service can be promoted to a foreground service, i.e., the service needs to
        // be officially started (which we do here).
        startService(Intent(applicationContext, ForegroundOnlyLocationService::class.java))

        try {
            // TODO: Step 1.5, Subscribe to location changes.
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.myLooper())
        } catch (unlikely: SecurityException) {
            SharedPreferenceUtil.saveLocationTrackingPref(this, false)
            foregroundServiceSubscribeLocationUpdate = false
            Log.e(TAG, "Lost location permissions. Couldn't remove updates. $unlikely")
        }
    }

    fun unsubscribeToLocationUpdates() {
        Log.d(TAG, "unsubcribeToLocationUpdates ----")

        try{
            // Unsubscribe to Location changes
            val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            removeTask.addOnCompleteListener {
                if(it.isSuccessful) {
                    Log.d(TAG, "Location callback removed")
                    stopSelf()
                }
                else {
                    Log.d(TAG, "Failed to remove Location callback")
                }

            }
            SharedPreferenceUtil.saveLocationTrackingPref(this, false)
            foregroundServiceSubscribeLocationUpdate = false
        } catch (unlikely: SecurityException) {
            SharedPreferenceUtil.saveLocationTrackingPref(this, true)
            foregroundServiceSubscribeLocationUpdate = true
            Log.e(TAG, "Lost location permissions. Couldn;t remove updates")
        }
    }

    private fun generateNotification(location: Location?): Notification {
        Log.d(TAG, "generateNotification() ---- ")

        // 1. get Data
        val mainNotificationText = location.toString()
        val titleTExt = "Location in Android"

        // 2. Create Notification Channel for O+ and beyond devices (26+)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, titleTExt, NotificationManager.IMPORTANCE_DEFAULT)

            // Adds Notification channel to system. Attempting to create an exisitng channel
            // with its original performs no operator, so it's safe to perform the blow sequence.
            notificationManager.createNotificationChannel(notificationChannel)
        }

        // 3. Build the BIG_TEXT_STYLE
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(mainNotificationText)
            .setBigContentTitle(titleTExt)

        // 4. Set up the main Intent.Pending intents for notification
        val launchActivityIntent = Intent(this, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            this, 0, launchActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val cancelIntent = Intent(this, ForegroundOnlyLocationService::class.java)
        cancelIntent.putExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, true)
        val servicePendingIntent = PendingIntent.getService(
            this, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        // 5. Build and issue the notification
        val notificationCompatbuuilder =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)

        return notificationCompatbuuilder
            .setStyle(bigTextStyle)
            .setContentTitle(titleTExt)
            .setContentText(mainNotificationText)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(
                R.drawable.ic_launch, "Launch activity",
                activityPendingIntent
            )
            .addAction(
                R.drawable.ic_cancel,
                getString(R.string.stop_location_updates_button_text),
                servicePendingIntent
            )
            .build()
    }

    companion object {
        private const val TAG = "TDebug"

        private const val PACKAGE_NAME = "com.tantnt.android.runstatistic"

        internal const val ACTION_FORGROUND_ONLY_LOCATION_BROADCAST =
            "$PACKAGE_NAME.action.FOREGROUND_ONLY_LOCATION_BROADCAST"

        internal const val EXTRA_LOCATION = "$PACKAGE_NAME.extra.LOCATION"

        private const val EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION =
            "$PACKAGE_NAME.extra.CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION"

        private const val NOTIFICATION_ID = 170491

        private const val NOTIFICATION_CHANNEL_ID = "while_in_use_channel_01"
    }


}