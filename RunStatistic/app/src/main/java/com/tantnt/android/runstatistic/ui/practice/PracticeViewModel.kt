package com.tantnt.android.runstatistic.ui.practice

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tantnt.android.runstatistic.base.isPracticeRunning
import com.tantnt.android.runstatistic.database.getDatabase
import com.tantnt.android.runstatistic.models.PracticeModel
import com.tantnt.android.runstatistic.repository.RunstatisticRepository
import kotlinx.coroutines.*


/** A method to download json data from url  */

class PracticeViewModel(application: Application) : AndroidViewModel(application) {

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
    private val runstatisticRepository = RunstatisticRepository(getDatabase(application))

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
        isPracticeRunning?.let {
            viewModelScope.launch {
                prac = runstatisticRepository.getLatestPracticeNonLive()
            }
        }
        return prac
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}