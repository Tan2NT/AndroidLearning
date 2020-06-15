package com.tantnt.android.runstatistic.base.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.tantnt.android.runstatistic.R
import com.tantnt.android.runstatistic.utils.sendNotification

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        var message = intent.getStringExtra(context.getString(R.string.notification_extra_message))

        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendNotification(
            message,
            context
        )

    }

}