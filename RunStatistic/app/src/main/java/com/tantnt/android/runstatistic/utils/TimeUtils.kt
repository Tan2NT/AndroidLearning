package com.tantnt.android.runstatistic.utils

import java.util.*
import java.util.concurrent.TimeUnit

val ONE_MINUTE_MILLI = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)
val ONE_HOUR_MILLI = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

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
        val second = TimeUnit.SECONDS.convert(durationMilli, TimeUnit.MILLISECONDS)
        val minutes = TimeUnit.MINUTES.convert(durationMilli, TimeUnit.MILLISECONDS)
        val hours = TimeUnit.HOURS.convert(durationMilli, TimeUnit.MILLISECONDS)
        return String.format("%02d:%02d", hours, minutes)
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