package com.tantnt.android.runstatistic.utils

import android.util.Log

object KcalCaclator {

    // https://www.womanandhome.com/health-and-wellbeing/calories-burned-walking-206766/
    fun burnedByWalkingPerMinute(
        bodyWeight: Double,         // in kilograms
        veloctity: Double,          // km/h must to convert to m/s
        bodyHeight: Double        // metres
    ) : Double {
        // convert velocity to from km/h to m/s
        val speed = veloctity / 3.6

        //First, multiply your body weight in kilograms (i.e. 60) by 0.035.
        var k1 = 0.035 * bodyHeight

        //Now, square your velocity (or speed) in metres per second, i.e. multiply it by itself.
        val k2 = speed  * speed

        // Now, divide the result by your height in metres (i.e. 1.6).
        val k3 = k2 / bodyHeight

        // Calories burned per minute
        val result =  k1 + k3 * 0.029 * 60

        Log.i("TDebug", "burnedByWalkingPerMinute: " + result.around2Place().toString())

        return result.around2Place()
    }
}