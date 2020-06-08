package com.tantnt.android.runstatistic.models

import android.content.res.Resources
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
        when(practiceType) {
            PRACTICE_TYPE.WALKING -> return "Walking"
            PRACTICE_TYPE.RUNNING -> return "Running"
            PRACTICE_TYPE.CYCLING -> return "Cycling"
        }
    }

    fun getStatusString(): String {
        when(status) {
            PRACTICE_STATUS.RUNNING       -> return getTypeString()
            PRACTICE_STATUS.NOT_ACTIVE    -> return "You are not active"
            PRACTICE_STATUS.PAUSING       -> return "Pausing"
            PRACTICE_STATUS.COMPETED      -> return "Completed"
            else                          -> return "You are not active"
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
