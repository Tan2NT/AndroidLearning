package com.tantnt.android.runstatistic.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tantnt.android.runstatistic.data.database.PracticeDao
//import com.tantnt.android.runstatistic.data.database.getDatabase
import com.tantnt.android.runstatistic.models.PracticeModel
import com.tantnt.android.runstatistic.data.repository.RunstatisticRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

private const val LIMIT_PRACTICE_COUNT = 30

class HomeViewModel @Inject constructor(
    practiceDao: PracticeDao
) : ViewModel() {

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
     * today practices -- all practices performed this day
     */
    val todayPractices : LiveData<List<PracticeModel>> = runstatisticRepository.todayPractices


    /**
     * only get the best practice from 30 latest practices
     */
    val latest7DaysPractice : LiveData<List<PracticeModel>> = runstatisticRepository.latest7DaysPractices

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}