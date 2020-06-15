package com.tantnt.android.runstatistic.utils

import android.app.Notification
import android.app.Notification.BigTextStyle
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.tantnt.android.runstatistic.R
import com.tantnt.android.runstatistic.base.service.ForegroundOnlyLocationService
import com.tantnt.android.runstatistic.base.service.LaunchAppService
import com.tantnt.android.runstatistic.models.PRACTICE_TYPE
import com.tantnt.android.runstatistic.models.PracticeModel

val NOTIFICATION_NORMAL_ID = 0
val NOTIFICATION_PRACTICE_ID = 0

object NotificationUtils {
    fun createChannel(context: Context, channelId: String, channelName: String, chanelDes: String) {
        // TODO: Step 1.6 START create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                // TODO: Step 2.4 change importance
                NotificationManager.IMPORTANCE_HIGH
            )// TODO: Step 2.6 disable badges for this channel

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = chanelDes

            val notificationManager = context.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }
        // TODO: Step 1.6 END create channel
    }

    fun generatePracticeNotification(appContext: Context, practice: PracticeModel): Notification {
        Log.d(LOG_TAG, "generateNotification() ---- ")

        // 1. get Data
        val titleTExt = "Location in Android"

        // 2. Create Notification Channel for O+ and beyond devices (26+)
        NotificationUtils.createChannel(appContext,
            appContext.getString(R.string.notification_practice_channel_id),
            appContext.getString(R.string.notification_practice_channel_name),
            appContext.getString(R.string.notification_practice_channel_description))

        // 3. custom notification
        val notificationLayout = RemoteViews(PACKAGE_NAME, R.layout.custom_notification_practice)
        notificationLayout.setTextViewText(R.id.textView_distance_value,
            practice?.distance!!.around3Place().toString() + " Km")
        var  gallon : String = appContext.getString(R.string.minte)
        if((practice?.duration!!.toDouble() / ONE_HOUR_MILLI).toInt() > 0)
            gallon = appContext.getString(R.string.hour)
        notificationLayout.setTextViewText(R.id.textView_time_value,
            TimeUtils.convertDutationToFormmated(practice?.duration!!).toString() + " " + gallon)
        notificationLayout.setTextViewText(R.id.textView_practice_status, practice?.getStatusString(appContext))

        var resId = R.drawable.walking_selected_icon
        when(practice?.practiceType) {
            PRACTICE_TYPE.RUNNING -> resId = R.drawable.running_selected_icon
            PRACTICE_TYPE.CYCLING -> resId = R.drawable.cycling_selected_icon
            PRACTICE_TYPE.WALKING -> resId = R.drawable.walking_selected_icon
        }
        notificationLayout.setImageViewResource(R.id.practice_icon, resId)

        // 4. Set up the main Intent.Pending intents for notification
        val launchActivityIntent = Intent(appContext, LaunchAppService::class.java)
        val activityPendingIntent = PendingIntent.getService(
            appContext, NOTIFICATION_PRACTICE_ID, launchActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val cancelIntent = Intent(appContext, ForegroundOnlyLocationService::class.java)
        cancelIntent.putExtra(ForegroundOnlyLocationService.EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, true)
        val servicePendingIntent = PendingIntent.getService(
            appContext, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        // 5. Build and issue the notification
        val notificationCompatbuilder =
            NotificationCompat.Builder(appContext,
                appContext.getString(R.string.notification_practice_channel_id)
            )

        val nof =  notificationCompatbuilder
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        nof.contentIntent = activityPendingIntent
        nof.deleteIntent = servicePendingIntent

        return nof
    }
}


fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

    val notificationManager: NotificationManager
    notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val titleText = applicationContext.getString(R.string.app_name)

    // Create channel for normal notification
    NotificationUtils.createChannel(applicationContext,
        applicationContext.getString(R.string.notification_channel_id),
        applicationContext.getString(R.string.notification_channel_name),
        applicationContext.getString(R.string.notification_channel_description))


    // Setup launch intent
    val launchActivityIntent = Intent(applicationContext, LaunchAppService::class.java)
    val activityPendingIntent = PendingIntent.getService(
        applicationContext,
        NOTIFICATION_NORMAL_ID, launchActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT
    )

    // Bui and issue the notification
    val notificationCompatbuilder =
        NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.notification_channel_id)      )

    val nof =  notificationCompatbuilder
        .setContentText(messageBody)
        .setContentTitle(titleText)
        .setStyle(NotificationCompat.BigTextStyle()
            .bigText(messageBody))
        .setSmallIcon(R.drawable.notification_icon)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .build()

    nof.contentIntent = activityPendingIntent

    notificationManager.notify(NOTIFICATION_NORMAL_ID, nof)

}

fun NotificationManager.cancalAllNotification() {
    cancelAll()
}