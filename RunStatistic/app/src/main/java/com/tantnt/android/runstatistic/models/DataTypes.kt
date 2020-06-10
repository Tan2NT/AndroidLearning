package com.tantnt.android.runstatistic.models

enum class PRACTICE_STATUS (val value: Int) {
    COMPETED(0),
    ACTIVE(1),
    PAUSING(2),
    NOT_ACTIVE(3)
}

enum class PRACTICE_TYPE (val value: Int) {
    WALKING (0),
    RUNNING (1),
    CYCLING (2)
}

object DAILY_TARGET {
    const val UNDER_MEDIUM = 2000
    const val MEDIUM = 6000
    const val HIGH_ACTIVE = 10000
    const val WEIGHT_LOSS = 15000
    const val MUSCLE_GAIN = 20000
}