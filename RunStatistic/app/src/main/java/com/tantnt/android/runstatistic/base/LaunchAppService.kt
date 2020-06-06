package com.tantnt.android.runstatistic.base

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.tantnt.android.runstatistic.utils.CLASS_NAME
import com.tantnt.android.runstatistic.utils.LOG_TAG
import com.tantnt.android.runstatistic.utils.PACKAGE_NAME

class LaunchAppService() : IntentService("LaunchAppService") {
    override fun onHandleIntent(intent: Intent?) {
        Log.i(LOG_TAG, "LaunchAppService:onHandleIntent ---- ")
        intent?.let {
            val notificationIntent = Intent()
            notificationIntent.setClassName(PACKAGE_NAME, CLASS_NAME)
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            notificationIntent.replaceExtras(intent)

            notificationIntent.setAction(Intent.ACTION_MAIN)
            notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER)

            startActivity(notificationIntent)
        }
    }

}