package com.tantnt.android.runstatistic.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.tantnt.android.runstatistic.base.FirebaseUserLiveData
import com.tantnt.android.runstatistic.utils.LOG_TAG

class ProfileViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATED
    }

    val authenticationState: LiveData<AuthenticationState> = FirebaseUserLiveData().map {user ->
        Log.i(LOG_TAG, "firebase live data ${user?.toString()}")
        if(user != null) {
            AuthenticationState.AUTHENTICATED
        }
        else {
            AuthenticationState.UNAUTHENTICATED
        }
    }
}