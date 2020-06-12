package com.tantnt.android.runstatistic

import android.app.Application
import android.util.Log
import com.facebook.ads.AudienceNetworkAds
import com.tantnt.android.runstatistic.utils.LOG_TAG

/**
 *
 */
class RunStatisticApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        //Initizlize the Audience Network SDK
        AudienceNetworkAds.initialize(this)
    }
}