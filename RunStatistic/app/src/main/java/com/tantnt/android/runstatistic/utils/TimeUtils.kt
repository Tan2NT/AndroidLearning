package com.tantnt.android.runstatistic.utils

import android.util.Log
import com.tantnt.android.runstatistic.network.service.TAG
import java.util.*
import java.util.concurrent.TimeUnit

object TimeUtils {
    fun getTimeInMilisecond() : Long {
        return System.currentTimeMillis()
    }

    fun getDurationTimeMilli(startTimeMilli : Long, endTimeMilli: Long): Long {
        return endTimeMilli - startTimeMilli
    }

    fun getDurationTimeMilliFrom(startTimeMilli : Long): Long {
        return System.currentTimeMillis() - startTimeMilli
    }

    fun convertDutationToFormmated(durationMilli: Long): String {
        val second = (durationMilli / ONE_SECOND_MILLI).toInt() % 60
        val minutes = (durationMilli / ONE_MINUTE_MILLI) % 60
        val hours =  (durationMilli / ONE_HOUR_MILLI)

        Log.i(TAG, "convert duration: $durationMilli to $hours:$minutes:$second")
        if(hours > 0)
            return String.format("%02d:%02d:%02d", hours, minutes, second)
        else
            return String.format("%02d:%02d", minutes, second)
    }

    fun getDateFromMilli(timeMilli: Long) : Date {
        var currentTime = System.currentTimeMillis()
        val tz = TimeZone.getDefault()
        val cal = GregorianCalendar.getInstance(tz)
        val offsetInMillis = tz.getOffset(cal.timeInMillis)

        currentTime -= offsetInMillis.toLong()
        val date = Date(currentTime)
        return date
    }

}