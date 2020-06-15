package com.tantnt.android.runstatistic.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.tantnt.android.runstatistic.models.PRACTICE_STATUS
import com.tantnt.android.runstatistic.models.PRACTICE_TYPE
import com.tantnt.android.runstatistic.models.PracticeModel
import org.threeten.bp.LocalDateTime

/**
 * Database entities go in this file.
 * These are responsible for reading and writing from the database.
 */

/**
 * DatabasePractice represents a practice in the database
 */

@Entity
data class DatabasePractice constructor(
    @PrimaryKey(autoGenerate = false)
    var start_time: LocalDateTime,          // timestamp    - start time of this practice
    var practice_type: PRACTICE_TYPE,       // type         - walking/running/cycling
    var duration: Long,                     // in second    - duration of this practice
    var distance: Double,                   // in Km        - how far which the user has been passed
    var calo: Double,                       // in Kcal      - how much Calo user has spent
    var speed: Double,                      // km/h         - the average speed of user during the practice
    var status: PRACTICE_STATUS,            //             - the status of this practice
    var path : ArrayList<LatLng>            //             - list of location user has pass through, use to draw the route
) {
}

/**
 * Map DatabasePractice to model practice
 */

fun List<DatabasePractice>.asModel(): List<PracticeModel> {
    return map {
        PracticeModel(
            startTime = it.start_time,
            practiceType = it.practice_type,
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
        startTime = this.start_time,
        practiceType = this.practice_type,
        duration = this.duration,
        distance = this.distance,
        calo = this.calo,
        speed = this.speed,
        status = this.status,
        path = this.path
    )
}
