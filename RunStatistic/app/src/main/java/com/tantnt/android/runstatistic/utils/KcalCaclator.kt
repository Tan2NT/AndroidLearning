package com.tantnt.android.runstatistic.utils

object KcalCaclator {

    // https://www.womanandhome.com/health-and-wellbeing/calories-burned-walking-206766/
    fun burnedByyWalkingPerMinute(
        bodyWeight: Double,         // in kilograms
        veloctity: Double,          // metres per second
        bodyHeight: Double        // metres
    ) : Double {
        //First, multiply your body weight in kilograms (i.e. 60) by 0.035.
        var k1 = 0.035 * bodyHeight

        //Now, square your velocity (or speed) in metres per second, i.e. multiply it by itself.
        val k2 = veloctity  * veloctity

        // Now, divide the result by your height in metres (i.e. 1.6).
        val k3 = k2 / bodyHeight

        // Calories burned per minute
        return k1 + k3 * 0.029 * 60
    }
}