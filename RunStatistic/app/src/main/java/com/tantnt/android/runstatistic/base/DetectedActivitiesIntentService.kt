package com.tantnt.android.runstatistic.base

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.ActivityRecognitionClient
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity

/**
 * IntentService for handling incoming intents that are generated as a result of requesting activities updates using
 */

var gDetectedActivities : MutableList<DetectedActivity> = arrayListOf()

class DetectedActivitiesIntentService : IntentService("DetectedActivitiesIntentService") {

    val LOCAL_TAG = "DetectedActivities"

    /**
     * Handle incoming intents.
     * @param intent The intent is provided (inside a PendingIntent) when requestActivityUpdates() is called
     */
    override fun onHandleIntent(intent: Intent?) {
        val result = ActivityRecognitionResult.extractResult(intent)

        // Get the list of the probable activities associated with the current state of the device.
        // Each activity is associated with a confidence level, which is an int between 0 and 100
        gDetectedActivities = result.probableActivities
        Log.i(LOCAL_TAG, "activities detected " + gDetectedActivities.toString())

        for(da in gDetectedActivities) {
            Log.i(LOCAL_TAG, "type: $da.type - confidence: $da.confidence - description: ${da.describeContents()}")
        }
    }

    override fun onCreate() {
        super.onCreate()

        Log.i(LOCAL_TAG, "onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.i(LOCAL_TAG, "onDestroy")
    }

}