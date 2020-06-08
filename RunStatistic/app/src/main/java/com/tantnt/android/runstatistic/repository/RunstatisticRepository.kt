package com.tantnt.android.runstatistic.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.tantnt.android.runstatistic.database.DatabasePractice
import com.tantnt.android.runstatistic.database.PracticeDatabase
import com.tantnt.android.runstatistic.database.asModel
import com.tantnt.android.runstatistic.models.PracticeModel
import com.tantnt.android.runstatistic.utils.LOG_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for storing the practice on disk
*/

class RunstatisticRepository(private val database: PracticeDatabase) {

    /**
     * latestPractice LiveData
     */
    val latestPractice: LiveData<PracticeModel> = Transformations.map(database.practiceDao.getlatestPractice()) {
        it?.let {
            it.asModel()
        }
    }

    /**
     * latestPractice non-Live
     */
    /**
     * latestPractice
     */
    suspend fun getLatestPracticeNonLive() : PracticeModel? {
        var practice : PracticeModel? = null
        withContext(Dispatchers.IO) {
            practice = database.practiceDao.getlatestPracticeNonLive()?.asModel()
        }
        return practice
    }

    /**
     * Insert/update practice in the database
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using 'withContext'
     * this function is now safe to call from any thread including the Main thread.
     */
    suspend fun insertPractice(practice: DatabasePractice) {
        withContext(Dispatchers.IO) {
            Log.i(LOG_TAG, "insertPractice --- ")
            database.practiceDao.insert(practice)
        }
    }
}
