package com.tantnt.android.runstatistic.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tantnt.android.runstatistic.database.getDatabase
import com.tantnt.android.runstatistic.models.PracticeModel
import com.tantnt.android.runstatistic.repository.RunstatisticRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

private const val LIMIT_PRACTICE_COUNT = 30

class HomeViewModel(application: Application) : ViewModel() {

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
     * today practices -- all practices performed this day
     */
    val todayPractices : LiveData<List<PracticeModel>> = runstatisticRepository.todayPractices

    private val _latestPractices = MutableLiveData<List<PracticeModel>>()
    val latestPractoces : LiveData<List<PracticeModel>>
        get() = _latestPractices

    /**
     * only get the best practice from 30 latest practices
     */
    val latest30Practice = runstatisticRepository.latest30Practices
}