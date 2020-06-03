package com.tantnt.android.runstatistic.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TIME (
    var HOUR : Int,
    var MINUTE : Int,
    var SECOND : Int) {

    fun gettring() : String {
        var minute = MINUTE.toString()
        if (MINUTE < 10)
            minute = "0" + minute

        var seconds = SECOND.toString()
        if (SECOND < 10)
            seconds = "0" + seconds

        return "$HOUR : $minute + : $seconds"
    }
}

object TimeUtils {

    fun getTimeInMilisecond() : Long {
        return System.currentTimeMillis()
    }

    fun getElapseTime(from : Long, to: Long): Long {
        return to - from
    }

    fun getElapseTimeFrom(from: Long) : Long {
        return getTimeInMilisecond() - from
    }

    // convert milliseconds to string
    fun getDateStringFromMilliseconds(value: Long): String {
        val simpleDateFormat = SimpleDateFormat("MM dd,yyyy HH:mm")
        var resultDate = Date(value)
        return simpleDateFormat.format(resultDate)

        System.nanoTime()
    }

    // in milliseconds
/*    fun calculateElapsedTime(start: Long, end: Long) : Long {
    }*/

    // convert elapse time to TIME(Hour:Minute:Second)
/*    fun convertElapseTimeToTIME(elapse: Long) : TIME {

    }*/
}