package com.tantnt.android.runstatistic.ui.history

import androidx.lifecycle.ViewModel
import com.tantnt.android.runstatistic.data.database.PracticeDao
//import com.tantnt.android.runstatistic.data.database.getDatabase
import com.tantnt.android.runstatistic.data.repository.RunstatisticRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

class HistoryViewModel @Inject constructor(practiceDao: PracticeDao) : ViewModel() {

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
    private val runstatisticRepository = RunstatisticRepository(practiceDao)

    /**
     * Get all practices
     */

    val allPractices = runstatisticRepository.allPractices

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
