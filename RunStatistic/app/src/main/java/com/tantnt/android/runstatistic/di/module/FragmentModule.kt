package com.tantnt.android.runstatistic.di.module

import com.tantnt.android.runstatistic.ui.history.HistoryFragment
import com.tantnt.android.runstatistic.ui.home.HomeFragment
import com.tantnt.android.runstatistic.ui.practice.PracticeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    /**
     * Define the name of the Fragment that we are going to inject the ViewModelFactory into
     * e.r. in our case, the name of the fragment: HomeFragment, HistoryFragment, PracticeFragment
     */

    @ContributesAndroidInjector ()
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector ()
    abstract fun contributePracticeFragment(): PracticeFragment

    @ContributesAndroidInjector ()
    abstract fun contributeHistoryFragment(): HistoryFragment
}