package com.tantnt.android.runstatistic.utils

import android.location.Location
import androidx.lifecycle.MutableLiveData

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