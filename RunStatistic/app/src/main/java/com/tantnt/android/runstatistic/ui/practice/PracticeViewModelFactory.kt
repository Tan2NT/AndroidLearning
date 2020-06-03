package com.tantnt.android.runstatistic.ui.practice

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory for constructing PracticeViewModel in parameter
 */
class PracticeViewModelFactory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PracticeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PracticeViewModel(app) as T
        }
        throw IllegalArgumentException("Unable to construc PracticeViewModel")
    }

}