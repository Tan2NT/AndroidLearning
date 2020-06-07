package com.tantnt.android.runstatistic.models

import com.google.android.gms.maps.model.LatLng
import com.tantnt.android.runstatistic.database.DatabasePractice

/**
 * Models objects are plain Kotlin data classes that represent the thing in our app. These are the
 * objects that should be displayed on screen, or manipulated by the app.
 *
 * @see database for objects that are mapped to the database
 */

/**
 * Practices represent a practice that can be displayed.
 */

data class PracticeModel(
    var startTime : Long,
    var practiceType: PRACTICE_TYPE,
    var duration: Double,
    var distance: Double,
    var calo: Double,
    var speed: Double,
    var status: PRACTICE_STATUS,
    var path: ArrayList<LatLng>) {

    fun getTypeString() : String {
        //  todo: adding type then return base on this field
        return "Walking"
    }

    fun getStatusString(): String {
        when(status) {
            PRACTICE_STATUS.RUNNING       -> return "running"
            PRACTICE_STATUS.NOT_ACTIVE    -> return "you are not active"
            PRACTICE_STATUS.PAUSING       -> return "pausing"
            else                          -> return "you are not active"
        }
    }
}

fun PracticeModel.asDatabasePractice() : DatabasePractice {
    return DatabasePractice(
        this.startTime,
        this.practiceType,
        this.duration,
        this.distance,
        this.calo,
        this.speed,
        this.status,
        this.path
    )
}
