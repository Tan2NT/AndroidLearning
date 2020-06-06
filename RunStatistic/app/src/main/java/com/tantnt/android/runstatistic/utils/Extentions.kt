package com.tantnt.android.runstatistic.utils

import android.location.Location
import androidx.lifecycle.MutableLiveData
import java.math.RoundingMode
import java.text.DecimalFormat

// notify observer a LiveData
fun <T> MutableLiveData<T>.nofifyObserver() {
    this.value = this.value
}

fun Location?.toString() : String {
    return if (this != null) {
        "$latitude, $longitude"
    } else {
        "Unknow location"
    }
}

fun Double.around2Place() : Double {
    //return Math.round(this * 100.0) / 100.0
    val decimalFormat = DecimalFormat("#.##")
    decimalFormat.roundingMode = RoundingMode.CEILING
    val stringValue = decimalFormat.format(this).replace(',', '.')
    return stringValue.toDouble()
}

fun Double.around3Place() : Double {
    //return Math.round(this * 1000.0) / 1000.0
    val decimalFormat = DecimalFormat("#.###")
    decimalFormat.roundingMode = RoundingMode.CEILING
    val stringValue = decimalFormat.format(this).replace(',', '.')
    return stringValue.toDouble()
}