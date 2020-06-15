package com.tantnt.android.runstatistic.utils

import java.util.concurrent.TimeUnit

/**
 * Define some constant values using in this app
 */

const val USE_GOOGLE_DIRECTIONS_SERVICE = false

const val LOG_TAG = "TDebug"

// Temporary
const val USER_HEIGHT_DEFAULT = 1.70
const val USER_WEIGHT_DEFAULT = 60.0

// Time

val ONE_SECOND_MILLI = 1000L  //TimeUnit.SECONDS.convert(1, TimeUnit.SECONDS)
val ONE_MINUTE_MILLI = ONE_SECOND_MILLI * 60L //TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)
val ONE_HOUR_MILLI = ONE_MINUTE_MILLI * 60L //TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

val DETECTION_INTERVAL_IN_MILLISECONDS: Long = 1    // hard this value to make the update not too long *8-15s)

const val PACKAGE_NAME = "com.tantnt.android.runstatistic"
const val CLASS_NAME = "$PACKAGE_NAME.MainActivity"