package com.tantnt.android.runstatistic.ui.practice

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.PolyUtil
import com.jakewharton.threetenabp.AndroidThreeTen.init
import com.tantnt.android.runstatistic.database.getDatabase
import com.tantnt.android.runstatistic.models.PracticeModel
import com.tantnt.android.runstatistic.network.service.GoogleDirectionsNetwork
import com.tantnt.android.runstatistic.repository.RunstatisticRepository
import com.tantnt.android.runstatistic.utils.MathUtils
import com.tantnt.android.runstatistic.utils.USE_GOOGLE_DIRECTIONS_SERVICE
import com.tantnt.android.runstatistic.utils.nofifyObserver
import kotlinx.coroutines.*
import org.json.JSONObject
import java.time.LocalDateTime


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

    val _grade = MutableLiveData<Int>()
    val grade : LiveData<Int>
        get() = _grade

    /**
     * called immediately when this ViewModel is created
     */
    init {
        Log.d(TAG, "PracticeViewModel Init ---- ")
        _grade.value = 2
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}