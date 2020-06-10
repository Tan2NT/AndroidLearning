package com.tantnt.android.runstatistic.models

import org.threeten.bp.LocalDate

class PracticeDayInfo (
    var date: LocalDate,
    var totalDistance : Double,
    var totalTimeSpent: Long,
    var totalStepCounted: Int,
    var totalCaloBurned: Double) {
}