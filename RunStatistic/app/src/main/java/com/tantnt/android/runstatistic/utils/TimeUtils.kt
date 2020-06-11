package com.tantnt.android.runstatistic.utils

import android.util.Log
import com.tantnt.android.runstatistic.network.service.TAG
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

/**
 * This class content some common functions need to reuse more than one time in this project
 */

object TimeUtils {
    fun getTimeInMilisecond() : Long {
        return System.currentTimeMillis()
    }

    fun getDurationTimeMilliFrom(startTimeMilli : Long): Long {
        return System.currentTimeMillis() - startTimeMilli
    }

    /**
     * Convert a duration time into hh/mm/ss format
     */
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

    /**
     * Convert a LocalDateTime to custom date time format. ex from 2020-06-10T17:45:55.9483536 to 2020-06-10 17:45:55
     */
    fun convertTimeToStringFormat(time: LocalDateTime) : String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH::mm")
        return time.format(formatter)
    }

    /**
     * Parse date time string to LocalDateTime
     */
    fun parseTimeFromString(timeString: String): LocalDateTime{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH::mm")
        return LocalDateTime.parse(timeString, formatter)
    }
}