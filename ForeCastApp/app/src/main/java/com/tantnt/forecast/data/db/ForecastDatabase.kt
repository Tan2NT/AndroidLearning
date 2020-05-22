package com.tantnt.forecast.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tantnt.forecast.data.db.entity.CurrentWeatherEntry
import com.tantnt.forecast.data.db.entity.DataTypeConvertor
import com.tantnt.forecast.data.db.entity.FutureWeatherEntry
import com.tantnt.forecast.data.db.entity.LocalDateTimeConverter

@Database(
    entities = [CurrentWeatherEntry::class, FutureWeatherEntry::class],
    version = 1
)
@TypeConverters(DataTypeConvertor::class, LocalDateTimeConverter::class)
abstract class ForecastDatabase : RoomDatabase(){
    abstract  fun currentWeatherDao() : CurrentWeatherDao
    abstract  fun futureWeatherDao() : FutureWeatherDao

    companion object {
        @Volatile private var instance : ForecastDatabase? = null
        private val LOCK = Any()

        operator  fun invoke(context: Context) = instance?: synchronized(LOCK){
            instance?: buildDatabase(context).also { instance = it}

        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                ForecastDatabase::class.java, "forecast.db").build()
    }
}