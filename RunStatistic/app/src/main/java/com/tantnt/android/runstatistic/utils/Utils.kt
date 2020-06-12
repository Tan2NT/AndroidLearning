package com.tantnt.android.runstatistic.utils

import android.R.attr
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.edit
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback
import com.tantnt.android.runstatistic.R
import com.tantnt.android.runstatistic.models.DAILY_TARGET
import com.tantnt.android.runstatistic.models.PracticeModel
import kotlinx.android.synthetic.main.fragment_practice_detail.view.*
import kotlinx.coroutines.*
import java.io.File


internal object SharedPreferenceUtil {

    const val KEY_FOREGROUND_ENABLED = "tracking_foreground_location"

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The [Context].
     */
    fun getLocationTrackingPref(context: Context): Boolean =
        context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            .getBoolean(KEY_FOREGROUND_ENABLED, false)

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
    fun saveLocationTrackingPref(context: Context, requestingLocationUpdates: Boolean) =
        context.getSharedPreferences(
            context.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE).edit {
            putBoolean(KEY_FOREGROUND_ENABLED, requestingLocationUpdates)
        }

    /**
     * get daily target step value from Share reference
     */

    const val KEY_DAILY_TARGET_STEP = "daily_target_step"
    fun getDailyTargetStep(context: Context): Int =
        context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            .getInt(KEY_DAILY_TARGET_STEP, DAILY_TARGET.MEDIUM)

    /**
     * Stores the Daily target step value in SharedPreferences.
     */
    fun saveDailyTargetStepPref(context: Context, target: Int) =
        context.getSharedPreferences(
            context.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE).edit {
            putInt(KEY_DAILY_TARGET_STEP, target)
        }
}