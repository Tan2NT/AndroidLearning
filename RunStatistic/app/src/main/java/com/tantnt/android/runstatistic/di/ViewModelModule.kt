package com.tantnt.android.runstatistic.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tantnt.android.runstatistic.di.module.ViewModelKey
import com.tantnt.android.runstatistic.ui.factory.ViewModelFactory
import com.tantnt.android.runstatistic.ui.history.HistoryViewModel
import com.tantnt.android.runstatistic.ui.home.HomeViewModel
import com.tantnt.android.runstatistic.ui.practice.PracticeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * This class is used t provide a map of view model throuh dagger that is used by the ViewModelFactory
 */

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    /**
     * This method basically says: inject this object into a Map using @IntoMap annotation
     * with the HomeViewModel class as key and a Provider that will build a HomeViewModel object
     */
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    protected  abstract fun homeViewModel(homeViewModel: HomeViewModel): ViewModel

    /**
     * This method basically says: inject this object into a Map using @IntoMap annotation
     * with the PracticeViewModel class as key and a Provider that will build a PracticeViewModel object
     */
    @Binds
    @IntoMap
    @ViewModelKey(PracticeViewModel::class)
    protected  abstract fun practiceViewModel(practiceViewModel: PracticeViewModel): ViewModel

    /**
     * This method basically says: inject this object into a Map using @IntoMap annotation
     * with the HistoryViewModel class as key and a Provider that will build a HistoryViewModel object
     */
    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel::class)
    protected  abstract fun historyViewModel(historyViewModel: HistoryViewModel): ViewModel


}