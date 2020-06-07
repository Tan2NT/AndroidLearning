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

val ONE_SECOND_MILLI = 1000  //TimeUnit.SECONDS.convert(1, TimeUnit.SECONDS)
val ONE_MINUTE_MILLI = ONE_SECOND_MILLI * 60 //TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)
val ONE_HOUR_MILLI = ONE_MINUTE_MILLI * 60 //TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

const val PACKAGE_NAME = "com.tantnt.android.runstatistic"
const val CLASS_NAME = "$PACKAGE_NAME.MainActivity"

enum class PRACTICE_STATUS (val value: Int) {
    COMPETED(0),
    RUNNING(1),
    NOT_RUNNING(2),
    PAUSING(3),
    NOT_ACTIVE(4)
}