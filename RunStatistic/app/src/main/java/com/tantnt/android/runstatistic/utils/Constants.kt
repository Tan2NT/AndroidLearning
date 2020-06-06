package com.tantnt.android.runstatistic.utils

const val USE_GOOGLE_DIRECTIONS_SERVICE = false

const val LOG_TAG = "TDebug"

// Temporary
const val USER_HEIGHT_DEFAULT = 1.70
const val USER_WEIGHT_DEFAULT = 60.0

enum class PRACTICE_STATUS (val value: Int) {
    COMPETED(0),
    RUNNING(1),
    NOT_RUNNING(2),
    PAUSING(3),
    NOT_ACTIVE(4)
}