package com.tantnt.android.runstatistic.di.module

import com.tantnt.android.runstatistic.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * adding the FragmentModule to the Activity which contains the fragment
 * This allow us to inject things into Activities using AndroidInjection.inject(this) in the onCreate method
 */

@Module
abstract class ActivityBuilderModule  {

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeMainActivity(): MainActivity

}