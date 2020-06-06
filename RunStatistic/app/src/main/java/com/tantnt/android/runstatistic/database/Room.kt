package com.tantnt.android.runstatistic.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Dao
interface PracticeDao {
    @Query("SELECT * FROM  databasepractice")
    fun getPractices(): LiveData<List<DatabasePractice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(practice: DatabasePractice)

    @Query("SELECT * FROM databasepractice WHERE status != 0 ORDER BY start_time DESC LIMIT 1")
    fun getlatestUncompletedPractice(): LiveData<DatabasePractice>

    @Query("SELECT * FROM databasepractice WHERE status != 0 ORDER BY start_time DESC LIMIT 1")
    fun getlatestUncompletedPracticeNonLive(): DatabasePractice

    // Todo: get practices by range of days/months/years
}

@Database(entities = [DatabasePractice::class], version = 2)
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
                //.addMigrations(MIGRATION_1_2)
                .build()
        }
    }
    return INSTANCE
}