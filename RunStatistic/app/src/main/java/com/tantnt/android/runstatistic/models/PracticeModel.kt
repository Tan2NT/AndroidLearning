package com.tantnt.android.runstatistic.models

import com.google.android.gms.location.DetectedActivity
import com.google.android.gms.maps.model.LatLng
import com.tantnt.android.runstatistic.base.gDetectedActivities
import com.tantnt.android.runstatistic.database.DatabasePractice
import com.tantnt.android.runstatistic.ui.view.PracticeViewItem
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

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
    var startTime : LocalDateTime,
    var practiceType: PRACTICE_TYPE,
    var duration: Long,
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

    /**
     * get Steps - return how many step user has been pass
     * assum that the number steps of eacj 1 metres perform is
     * 1 km of Walking = 2000 steps
     * 1 km of Running = 1500 steps
     * 1 km of cycling = 1000 steps
     */
    val STEPS_MAP : Map<PRACTICE_TYPE, Int> = mapOf(
        PRACTICE_TYPE.WALKING to 2000,
        PRACTICE_TYPE.RUNNING to 1500,
        PRACTICE_TYPE.CYCLING to 1000
        )

    fun getStepsCounted() : Int {
        val stepUnit = STEPS_MAP[practiceType]
        return (distance * stepUnit!!).toInt()
    }
}

/**
 * ---------------------- EXTENTION FUNCTIONS GOES HERE ----------------------
 */

/**
 * get Database data (DatabasePractice) from App data (PracticeModel)
 */
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

/**
 * Get ViewHolder data (PracticeviewItem) from App data (PracticeModel)
 * preparing the data to display into recycler view
 */
fun List<PracticeModel>.asListPracticeItem(): List<PracticeViewItem> {
    return this.map {
        PracticeViewItem(it)
    }
}

/**
 * get PracticeDayInfo of one practice day
 * NOTE: each practice in the list<PracticeModel) must have the same date(startDay)
 */
fun List<PracticeModel>.getPracticeDayInfo(): PracticeDayInfo {
    // find the best practice day

    if(this.size == 0)
        return  PracticeDayInfo(
            LocalDate.now(),
            0.0, 0L, 0, 0.0)
        var practiceDayInfo =
            PracticeDayInfo(this.get(0).startTime.toLocalDate(),
                0.0, 0L, 0, 0.0)
        this.forEach { practice ->
            practiceDayInfo.totalStepCounted += practice.getStepsCounted()
            practiceDayInfo.totalDistance += practice.distance
            practiceDayInfo.totalTimeSpent += practice.duration
            practiceDayInfo.totalCaloBurned += practice.calo
        }
        return practiceDayInfo
}
