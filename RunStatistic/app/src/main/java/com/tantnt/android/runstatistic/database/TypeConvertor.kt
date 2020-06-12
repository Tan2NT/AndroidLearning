package com.tantnt.android.runstatistic.database

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tantnt.android.runstatistic.models.PRACTICE_STATUS
import com.tantnt.android.runstatistic.models.PRACTICE_TYPE
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.sql.Timestamp
import kotlin.collections.ArrayList

class DataConvertor {

    private val gson = Gson()

    @TypeConverter
    fun stringToList(data: String): ArrayList<LatLng> {
        val listType = object : TypeToken<List<LatLng>>() {}.type
        return gson.fromJson<ArrayList<LatLng>> (data, listType)
    }

    @TypeConverter
    fun listToString(list : ArrayList<LatLng>) : String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun localDateTimeTosqlString(dateTime: LocalDateTime) : String {
        return dateTime.toString()
    }

    @TypeConverter
    fun strongToLocalDateTime(timeString: String) : LocalDateTime {
        return LocalDateTime.parse(timeString)
    }

    @TypeConverter
    fun practiceTypeToInt(type: PRACTICE_TYPE) : Int {
        return type.value
    }

    @TypeConverter
    fun intToPracticeType(value: Int) : PRACTICE_TYPE {
        when (value) {
            PRACTICE_TYPE.WALKING.value -> return PRACTICE_TYPE.WALKING
            PRACTICE_TYPE.RUNNING.value -> return PRACTICE_TYPE.RUNNING
            PRACTICE_TYPE.CYCLING.value -> return PRACTICE_TYPE.CYCLING
            else ->                        return PRACTICE_TYPE.WALKING
        }
    }

    @TypeConverter
    fun practiceStatusToInt(status: PRACTICE_STATUS) : Int {
        return status.value
    }

    @TypeConverter
    fun intToPracticeStatus(value: Int) : PRACTICE_STATUS {
        when (value) {
            PRACTICE_STATUS.COMPETED.value      -> return PRACTICE_STATUS.COMPETED
            PRACTICE_STATUS.ACTIVE.value       -> return PRACTICE_STATUS.ACTIVE
            PRACTICE_STATUS.NOT_ACTIVE.value    -> return PRACTICE_STATUS.NOT_ACTIVE
            PRACTICE_STATUS.PAUSING.value       -> return PRACTICE_STATUS.PAUSING
            else                                -> return PRACTICE_STATUS.NOT_ACTIVE
        }
    }
}