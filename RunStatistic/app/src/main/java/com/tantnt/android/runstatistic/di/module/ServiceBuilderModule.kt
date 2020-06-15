package com.tantnt.android.runstatistic.di.module

import com.tantnt.android.runstatistic.base.service.ForegroundOnlyLocationService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeLocationService(): ForegroundOnlyLocationService
}
