package com.tantnt.android.runstatistic.utils

import androidx.lifecycle.MutableLiveData

// notify observer a LiveData
fun <T> MutableLiveData<T>.nofifyObserver() {
    this.value = this.value
}