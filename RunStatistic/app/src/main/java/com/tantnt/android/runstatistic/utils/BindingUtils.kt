package com.tantnt.android.runstatistic.utils

import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.tantnt.android.runstatistic.base.isPracticeRunning
import com.tantnt.android.runstatistic.base.shouldShowPracticeResult
import com.tantnt.android.runstatistic.models.PRACTICE_STATUS
import com.tantnt.android.runstatistic.models.PracticeModel

/**
 * This class perform the data binding, make the code more shorter and cleaner, run faster
 * 1. Write the function to process data and set data for the view here, using @BindingAdapter
 * 2. In the layout file, set the relation to the view, example: app:practiceDurationFormetted="@{practiceViewModel.practice}"
 */

@BindingAdapter("practiceDurationFormatted")
fun TextView.setPracticeDurationFormatted(practice: PracticeModel?) {
    Log.i(LOG_TAG, "practioce duration string: ")
    practice?.let {
        if (isPracticeRunning || shouldShowPracticeResult) {
            text = TimeUtils.convertDutationToFormmated(practice.duration.toLong())
        } else
            text = "00:00"
    }
}

@BindingAdapter("practiceDistanceString")
fun TextView.setPracticeDistanceString(practice: PracticeModel?) {
    practice?.let {
        if (isPracticeRunning || shouldShowPracticeResult) {
            text = practice.distance.around2Place().toString()
        } else
            text = "0.00"       // default value
    }
}

@BindingAdapter("practiceCaloBurnedString")
fun TextView.setPracticeCaloBurnedString(practice: PracticeModel?) {
    practice?.let {
        if (isPracticeRunning || shouldShowPracticeResult) {
            text = practice.calo.around2Place().toString()
        } else
            text = "0.00"       // default value
    }
}

@BindingAdapter("practiceSpeedString")
fun TextView.setPracticeSpeedString(practice: PracticeModel?) {
    practice?.let {
        if (isPracticeRunning || shouldShowPracticeResult) {
            text = practice.speed.around2Place().toString()
        } else
            text = "0.00"       // default value
    }
}

@BindingAdapter("practiceStatusString")
fun TextView.setPracticeStatusString(practice: PracticeModel?) {
    Log.i(LOG_TAG, "practice duration string: ")
    practice?.let {
        if (isPracticeRunning || shouldShowPracticeResult) {
            Log.i(LOG_TAG, "practioce duration string: " + practice.status.toString())
            when(practice.status) {
                PRACTICE_STATUS.NOT_RUNNING -> text = "You are not active"
                PRACTICE_STATUS.RUNNING -> text = "Running"
                PRACTICE_STATUS.PAUSING -> text = "Pausing"
                PRACTICE_STATUS.COMPETED -> text = "Completed"
                else -> text = "You are not active"
            }
        } else
            text = "You are not active"
    }
}