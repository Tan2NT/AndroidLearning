package com.tantnt.android.runstatistic.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PracticeDao {
    @Query("SELECT * FROM  databasepractice")
    fun getPractices(): LiveData<List<DatabasePractice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(practice: DatabasePractice)

    @Query("SELECT * FROM databasepractice ORDER BY start_time DESC LIMIT 1")
    fun getLatestPractice(): LiveData<DatabasePractice>

    // Todo: get practices by range of days/months/years
}

@Database(entities = [DatabasePractice::class], version = 1)
@TypeConverters(DataConvertor::class)
abstract class PracticeDatabase() : RoomDatabase() {
    abstract val practiceDao : PracticeDao
}

private lateinit var INSTANCE: PracticeDatabase

fun getDatabase(context: Context): PracticeDatabase {
    synchronized(PracticeDatabase::class::java) {
        if(!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                PracticeDatabase::class.java,
                "practice")
                .build()
        }
    }
    return INSTANCE
}