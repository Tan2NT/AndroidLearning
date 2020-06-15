package com.tantnt.android.runstatistic.ui.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
//import com.tantnt.android.runstatistic.data.database.getDatabase
import java.lang.IllegalArgumentException
//
//class HomeViewModelFactory(val application: Application) : ViewModelProvider.Factory {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return HomeViewModel(getDatabase(application).practiceDao) as T
//        }
//        throw IllegalArgumentException("unable to construct HomeViewModel")
//    }
//
//}