package com.tantnt.android.runstatistic.models

enum class PRACTICE_STATUS (val value: Int) {
    COMPETED(0),
    RUNNING(1),
    NOT_RUNNING(2),
    PAUSING(3),
    NOT_ACTIVE(4)
}

enum class PRACTICE_TYPE (val value: Int) {
    WALKING (0),
    RUNNING (1),
    CYCLING (2)

}