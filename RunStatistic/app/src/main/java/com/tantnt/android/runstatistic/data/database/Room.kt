package com.tantnt.android.runstatistic.data.database

import androidx.room.*

@Database(entities = [DatabasePractice::class], version = 2, exportSchema = false)
@TypeConverters(DataConvertor::class)
abstract class PracticeDatabase constructor() : RoomDatabase() {
    abstract val practiceDao : PracticeDao
}

private lateinit var INSTANCE: PracticeDatabase
//
//fun getDatabase(context: Context): PracticeDatabase {
//    synchronized(PracticeDatabase::class::java) {
//        if(!::INSTANCE.isInitialized) {
//            INSTANCE = Room.databaseBuilder(
//                context.applicationContext,
//                PracticeDatabase::class.java,
//                "practice")
//                .build()
//        }
//    }
//    return INSTANCE
//}