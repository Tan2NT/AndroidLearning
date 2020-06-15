package com.tantnt.android.runstatistic.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.tantnt.android.runstatistic.data.database.DatabasePractice
import com.tantnt.android.runstatistic.data.database.PracticeDao
import com.tantnt.android.runstatistic.data.database.asModel
import com.tantnt.android.runstatistic.models.PracticeModel
import com.tantnt.android.runstatistic.utils.LOG_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for storing the practice on disk
*/
@Singleton
class RunstatisticRepository @Inject constructor(private val practiceDao : PracticeDao) {

    /**
     * latestPractice LiveData
     */
    val latestPractice: LiveData<PracticeModel> = Transformations.map(practiceDao.getlatestPractice()) {
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
            practice = practiceDao.getlatestPracticeNonLive().asModel()
        }
        return practice
    }

    val todayPractices: LiveData<List<PracticeModel>> = Transformations.map(practiceDao.getPracticeByDay(
        LocalDateTime.now())) {
        it?.let {
            it.asModel()
        }
    }

    /**
     * Get practices in specific date
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using 'withContext'
     * this function is now safe to call from any thread including the Main thread.
     */
    suspend fun getPracticesByDay(date: LocalDateTime): LiveData<List<PracticeModel>> {
        var practices : LiveData<List<PracticeModel>>? = null
        withContext(Dispatchers.IO) {
            Log.i(LOG_TAG, "getPracticesByDay --- ")
            practices = Transformations.map(practiceDao.getPracticeByDay(date)) {
                it?.asModel()
            }
        }
        return practices!!
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
            practiceDao.insert(practice)
        }
    }

    /**
     * Get latest practices limit by X practices
     */
    suspend fun getlatestPracticesLimitBy(limit: Int): LiveData<List<PracticeModel>> {
        var practices : LiveData<List<PracticeModel>>? = null
        withContext(Dispatchers.IO) {
            Log.i(LOG_TAG, "getPracticesLimitBy $limit --- ")
            practices = Transformations.map(practiceDao.getLatestPractices(limit)) {
                it?.asModel()
            }
        }
        return practices!!
    }

    /**
     * 30 latestPractices
     */
    val latest7DaysPractices : LiveData<List<PracticeModel>> = Transformations.map(practiceDao.getAllPracticesBetweenDates(
        LocalDate.now().minusDays(7),
        LocalDate.now()
    )) {
        it.asModel()
    }

    /**
     * all practices LiveData
     */
    val allPractices : LiveData<List<PracticeModel>> = Transformations.map(practiceDao.getAllPractices()) {
        it.asModel()
    }
}
