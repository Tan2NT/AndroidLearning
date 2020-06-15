package com.tantnt.android.runstatistic

import android.app.Activity
import android.app.Application
import android.app.Service
import com.tantnt.android.runstatistic.di.component.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject
import com.facebook.ads.AudienceNetworkAds
import dagger.android.AndroidInjector
import dagger.android.HasServiceInjector

/**
 * Use our AppComponent (now prefixed wiht Dragger)  to inject our Application class
 * This way a DispachingAndroidInjector is injected which is then returned when
 * a injector for an activity is requested
 */


//class RunStatisticApplication : Application() {
class RunStatisticApplication : Application(), HasActivityInjector, HasServiceInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var dispatchingAndroidServiceInjector: DispatchingAndroidInjector<Service>

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)

        //Initizlize the Audience Network SDK
        AudienceNetworkAds.initialize(this)

    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector
    override fun serviceInjector(): AndroidInjector<Service> = dispatchingAndroidServiceInjector

}
