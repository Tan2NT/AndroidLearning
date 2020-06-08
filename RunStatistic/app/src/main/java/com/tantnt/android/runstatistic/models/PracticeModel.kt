package com.tantnt.android.runstatistic.models

import com.google.android.gms.location.DetectedActivity
import com.google.android.gms.maps.model.LatLng
import com.tantnt.android.runstatistic.base.gDetectedActivities
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

    /**
     * Get type of the practice
     */

    fun getTypeString() : String {
        //  todo: adding type then return base on this field
        when(practiceType) {
            PRACTICE_TYPE.WALKING -> return "Walking"
            PRACTICE_TYPE.RUNNING -> return "Running"
            PRACTICE_TYPE.CYCLING -> return "Cycling"
        }
    }

    /**
     * Get the status of user
     */
    fun getStatusString(): String {
        when(status) {
            PRACTICE_STATUS.ACTIVE       -> return getTypeString()
            PRACTICE_STATUS.NOT_ACTIVE    -> return "You are not active"
            PRACTICE_STATUS.PAUSING       -> return "Pausing"
            PRACTICE_STATUS.COMPETED      -> return "Completed"
            else                          -> return "You are not active"
        }
    }

    /**
     * Check if user is Active
     */
    fun isUserActive(): Boolean {
        checkUserStatus()
        return status == PRACTICE_STATUS.ACTIVE
    }

    /**
     * check if user is perform the right activity match with the practive type his/her choice
     */
    fun checkUserStatus() {
        if(status == PRACTICE_STATUS.ACTIVE || status == PRACTICE_STATUS.NOT_ACTIVE) {
            var detectActivityType = DetectedActivity.WALKING
            when (practiceType) {
                PRACTICE_TYPE.RUNNING -> detectActivityType = DetectedActivity.RUNNING
                PRACTICE_TYPE.CYCLING -> detectActivityType = DetectedActivity.ON_BICYCLE
                else -> detectActivityType = DetectedActivity.WALKING
            }

            var found = false
            val MIN_CONFIDENCE_TRUST_ALLOW = 20
            for (da in gDetectedActivities) {
                if(da.type == detectActivityType && da.confidence >= MIN_CONFIDENCE_TRUST_ALLOW)
                    found = true
            }

            if(found){
                status = PRACTICE_STATUS.ACTIVE
            } else {
                status = PRACTICE_STATUS.NOT_ACTIVE
            }
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
