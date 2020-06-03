package com.tantnt.android.runstatistic.utils

const val USE_GOOGLE_DIRECTIONS_SERVICE = false

const val LOG_TAG = "TDebug"

// Temporary
const val USER_HEIGHT_DEFAULT = 1.70
const val USER_WEIGHT_DEFAULT = 60.0

enum class PRACTICE_STATUS (val value: Int) {
    COMPETED(2),
    RUNNING(3),
    NOT_RUNNING(4),
    PAUSING (5)
}