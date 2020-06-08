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