package com.tantnt.android.runstatistic.database

import androidx.lifecycle.Transformations.map
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.android.gms.maps.model.LatLng
import com.tantnt.android.runstatistic.models.PracticeModel

/**
 * Database entities go in this file.
 * These are responsible for reading and writing from the database.
 */

/**
 * DatabasePractice represents a practice in the database
 */

@Entity
data class DatabasePractice constructor(
    @PrimaryKey
    var start_time: Long,      // timestamp    - start time of this practice
    var duration: Int,          // in second    - duration of this practice
    var distance: Double,       // in Km        - how far which the user has been passed
    var calo: Double,           // in Kcal      - how much Calo user has spent
    var speed: Double,          // km/h         - the average speed of user during the practice
    var status: Int, //             - the status of this practice
    @TypeConverters(DataConvertor::class)
    var path : ArrayList<LatLng> //             - list of location user has pass through, use to draw the route
)

/**
 * Map DatabasePractice to model practice
 */

fun List<DatabasePractice>.asModel(): List<PracticeModel> {
    return map {
        PracticeModel(
            start_time = it.start_time,
            duration = it.duration,
            distance = it.distance,
            calo = it.calo,
            speed = it.speed,
            status = it.status,
            path = it.path
        )
    }
}

fun DatabasePractice.asModel() : PracticeModel {
    return PracticeModel(
        start_time = this.start_time,
        duration = this.duration,
        distance = this.distance,
        calo = this.calo,
        speed = this.speed,
        status = this.status,
        path = this.path
    )
}
