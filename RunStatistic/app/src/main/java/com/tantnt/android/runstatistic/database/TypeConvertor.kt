package com.tantnt.android.runstatistic.database

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class DataConvertor {

    private val gson = Gson()

    @TypeConverter
    fun stringToList(data: String): ArrayList<LatLng> {
        if(data == null) {
            return arrayListOf()
        }

        val listType = object : TypeToken<List<LatLng>>() {}.type
        return gson.fromJson<ArrayList<LatLng>> (data, listType)
    }

    @TypeConverter
    fun listToString(list : ArrayList<LatLng>) : String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun localDateToLong(date: LocalDate) : Long {
        if(date == null)
            return null!!
        return date.toEpochDay()
    }

    @TypeConverter
    fun longToLocalDate(epochTime : Long) : LocalDate {
        return LocalDate.ofEpochDay(epochTime)
    }

}