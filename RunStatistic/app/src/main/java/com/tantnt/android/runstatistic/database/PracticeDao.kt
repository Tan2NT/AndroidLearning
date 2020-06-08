package com.tantnt.android.runstatistic.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tantnt.android.runstatistic.models.PRACTICE_TYPE

@Dao
interface PracticeDao {
    @Query("SELECT * FROM  databasepractice")
    fun getPractices(): LiveData<List<DatabasePractice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(practice: DatabasePractice)

    @Query("SELECT * FROM databasepractice ORDER BY start_time DESC LIMIT 1")
    fun getlatestPractice(): LiveData<DatabasePractice>

    @Query("SELECT * FROM databasepractice ORDER BY start_time DESC LIMIT 1")
    fun getlatestPracticeNonLive(): DatabasePractice

    @Query("SELECT * FROM databasepractice WHERE practice_type == :type")
    fun getPracticeByType(type: PRACTICE_TYPE): DatabasePractice

    // Todo: get practices by range of days/months/years
}