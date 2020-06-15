package com.tantnt.android.runstatistic.ui.practice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tantnt.android.runstatistic.base.service.gIsPracticeRunning
import com.tantnt.android.runstatistic.data.database.PracticeDao
//import com.tantnt.android.runstatistic.data.database.getDatabase
import com.tantnt.android.runstatistic.models.PracticeModel
import com.tantnt.android.runstatistic.data.repository.RunstatisticRepository
import com.tantnt.android.runstatistic.utils.LOG_TAG
import kotlinx.coroutines.*
import javax.inject.Inject


/** A method to download json data from url  */

class PracticeViewModel @Inject constructor( praticeDao: PracticeDao ) : ViewModel() {

    private val TAG : String = "TDebug"

    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = SupervisorJob()

    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     *
     * Since we pass viewModelJob, you can cancel all coroutines launched by uiScope by calling
     * viewModelJob.cancel()
     */
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * the data source this ViewModel will fetch results from
     */
    private val runstatisticRepository = RunstatisticRepository(praticeDao)

    /**
     * a practice displayed on the screen
     */
    val practice : LiveData<PracticeModel> = runstatisticRepository.latestPractice

    /**
     * called immediately when this ViewModel is created
     */
    init {
        Log.d(TAG, "PracticeViewModel Init ---- ")
    }

    fun getCurrentPractice() : PracticeModel? {
        var prac : PracticeModel? = null
        gIsPracticeRunning.let {
            viewModelScope.launch {
                prac = runstatisticRepository.getLatestPracticeNonLive()
            }
        }
        return prac
    }

    override fun onCleared() {
        Log.i(LOG_TAG, "PracticeViewModel onCleared")
        super.onCleared()
        viewModelJob.cancel()
    }

}