package com.tantnt.android.runstatistic.models

import com.tantnt.android.runstatistic.ui.view.PracticeDayViewItem
import org.threeten.bp.LocalDate

class PracticeDayInfo (
    var date: LocalDate,
    var totalDistance : Double,
    var totalTimeSpent: Long,
    var totalStepCounted: Int,
    var totalCaloBurned: Double) {
}

fun List<PracticeDayInfo>.asPracticeDayViewItem() : List<PracticeDayViewItem> {
    return this.map {
        PracticeDayViewItem(
            it
        )
    }
}