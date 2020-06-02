package com.tantnt.android.runstatistic

import android.app.Application
import android.util.Log
import com.tantnt.android.runstatistic.utils.LOG_TAG

/**
 *
 */
class RunStatisticApplication : Application() {
    override fun onCreate() {
        Log.i(LOG_TAG, "RunStatisticApplication::onCreated() --- " )
        super.onCreate()
    }
}