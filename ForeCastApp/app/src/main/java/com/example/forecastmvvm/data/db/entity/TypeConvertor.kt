package com.example.forecastmvvm.data.db.entity

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.util.*

class DataTypeConvertor{
    private val gson = Gson()

    @TypeConverter
    fun stringToList(data: String?): List<String>{
        if(data == null){
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<String>>(){

        }.type

        return gson.fromJson<List<String>>(data, listType)
    }

    @TypeConverter
    fun weatherToString(data : WeatherDescriptionWeatherbit) : String {
        return gson.toJson(data)
    }

    @TypeConverter
    fun stringToWeather(data: String?) : WeatherDescriptionWeatherbit {

        val listType = object : TypeToken<WeatherDescriptionWeatherbit>(){

        }.type

        return gson.fromJson<WeatherDescriptionWeatherbit>(data, listType)
    }

    @TypeConverter
    fun listToString(someObjects : List<String>) : String {
        return gson.toJson(someObjects)
    }

    @TypeConverter
    fun jsonToString(someObject : JsonObject) : String {
        return gson.toJson(someObject)
    }
}

