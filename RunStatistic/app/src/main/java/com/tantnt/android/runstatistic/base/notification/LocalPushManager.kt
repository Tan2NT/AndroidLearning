package com.tantnt.android.runstatistic.base.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import androidx.core.app.AlarmManagerCompat
import com.tantnt.android.runstatistic.R
import com.tantnt.android.runstatistic.utils.LOG_TAG
import com.tantnt.android.runstatistic.utils.ONE_HOUR_MILLI
import com.tantnt.android.runstatistic.utils.ONE_MINUTE_MILLI

object LocalPushManager {
       private val REQUEST_CODE = 0

       fun scheduleNotification(context: Context) {
           //  schedule 1day, 2 days, ... to trigger notification to inform user come back the app to start practice
           setAlarm(context, context.getString(R.string.notification_not_practice_1_day), 1 * 24 * ONE_HOUR_MILLI)
       }

       fun setAlarm(context: Context, message: String, delayTime: Long) {
           // create an alarm intent
           val notifyIntent = Intent(context, AlarmReceiver::class.java)
           notifyIntent.putExtra(context.getString(R.string.notification_extra_message), message )

           val notifyPendingIntent = PendingIntent.getBroadcast(
               context,
               REQUEST_CODE,
               notifyIntent,
               PendingIntent.FLAG_UPDATE_CURRENT
           )

           val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
           AlarmManagerCompat.setExactAndAllowWhileIdle(
               alarmManager,
               AlarmManager.ELAPSED_REALTIME_WAKEUP,
               SystemClock.elapsedRealtime() + delayTime,
               notifyPendingIntent
           )
       }

    fun cancelAlarm(context: Context) {
        val notifyIntent = Intent(context, AlarmReceiver::class.java)
        val  notifyPendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(notifyPendingIntent)
    }
}