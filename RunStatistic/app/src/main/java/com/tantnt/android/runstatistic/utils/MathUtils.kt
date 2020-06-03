package com.tantnt.android.runstatistic.utils

import com.google.android.gms.maps.model.LatLng

object MathUtils {

    // distance in kilometer
    fun distance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515

        return (Math.round(dist * 100.0) / 100.0)
    }

    fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    // in Degrees
    fun angleBetweenTwoPoints(p1: LatLng, p2: LatLng) : Double {
        val dLon = p2.longitude - p1.longitude
        val y = Math.sin(dLon) * Math.cos(p2.latitude)
        val x = Math.cos(p1.latitude) * Math.sin(p2.latitude)
        - Math.sin(p1.latitude) * Math.cos(p2.latitude) * Math.cos(dLon)
        var brng = Math.toDegrees(Math.atan2(y, x))
        brng = (360 - ((brng + 360) % 360))
        return brng
    }

    fun timeBetween2EpochDays(start: Long, end: Long) : Long {
        return end - start
    }
}