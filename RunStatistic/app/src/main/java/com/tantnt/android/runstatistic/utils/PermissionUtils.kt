package com.tantnt.android.runstatistic.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object PermissionUtils {

    fun checkPermission(appContext: Context, permission: String) : Boolean {
        return ActivityCompat.checkSelfPermission(appContext, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissions(appContext: Context, permissions: Array<String>) : Boolean {
        for(permission in permissions){
            if (!checkPermission(appContext, permission))
                return false
        }
        return true
    }
}