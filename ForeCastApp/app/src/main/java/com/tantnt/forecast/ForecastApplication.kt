package com.tantnt.forecast

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tantnt.forecast.data.ApixuWeatherApiService
import com.tantnt.forecast.data.db.ForecastDatabase
import com.tantnt.forecast.data.network.ConnectivityInterceptor
import com.tantnt.forecast.data.network.ConnectivityInterceptorImpl
import com.tantnt.forecast.data.network.WeatherNetworkDataSource
import com.tantnt.forecast.data.network.WeatherNetworkDataSourceImpl
import com.tantnt.forecast.data.provider.LocationProvider
import com.tantnt.forecast.data.provider.LocationProviderImpl
import com.tantnt.forecast.data.provider.UnitProvider
import com.tantnt.forecast.data.provider.UnitProviderImpl
import com.tantnt.forecast.data.repository.ForecastRepository
import com.tantnt.forecast.data.repository.ForecastRepositoryImpl
import com.tantnt.forecast.ui.weather.current.CurrentWeatherViewModalFactory
import com.tantnt.forecast.ui.weather.future.detail.FutureDetailWeatherViewModalFactory
import com.tantnt.forecast.ui.weather.future.list.FutureListWeatherViewModalFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*
import org.threeten.bp.LocalDate


class ForecastApplication : Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {

        import(androidXModule(this@ForecastApplication ))

        bind() from singleton { ForecastDatabase(instance()) }
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao()}
        bind() from singleton { instance<ForecastDatabase>().futureWeatherDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { ApixuWeatherApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context> ()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
        bind<ForecastRepository>() with singleton { ForecastRepositoryImpl(instance(), instance(), instance(), instance(), "") }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind() from provider { CurrentWeatherViewModalFactory(instance(), instance()) }
        bind() from provider { FutureListWeatherViewModalFactory(instance(), instance()) }
        bind() from factory { detailDate : LocalDate -> FutureDetailWeatherViewModalFactory(detailDate, instance(), instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }

}