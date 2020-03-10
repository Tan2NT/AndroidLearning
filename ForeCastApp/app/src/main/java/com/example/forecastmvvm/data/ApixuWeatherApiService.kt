package com.example.forecastmvvm.data

import com.example.forecastmvvm.data.network.ConnectivityInterceptor
import com.example.forecastmvvm.data.network.response.CurrentWeatherResponseWeatherbit
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// weatherstack
//const val API_KEY = "f4d3a0af0c591b3b57c2883ec635d7db"
// http://api.weatherstack.com/current?access_key=f4d3a0af0c591b3b57c2883ec635d7db&query=New%20York&lang=en

// accuweather
//const val API_KEY = "VOYjRLH3GxA3o1sK4XGECEf7egvWyUmz"
//http://dataservice.accuweather.com/forecasts/v1/daily/1day/352954?apikey=VOYjRLH3GxA3o1sK4XGECEf7egvWyUmz

// weatherBit
//current: https://api.weatherbit.io/v2.0/current?city=Da nang&key=c7ce4a9cd7e64318b1a3aa17f42ba918
//daily 16day: https://api.weatherbit.io/v2.0/forecast/daily?city=danang&key=c7ce4a9cd7e64318b1a3aa17f42ba918

const val API_KEY = "c7ce4a9cd7e64318b1a3aa17f42ba918"


interface ApixuWeatherApiService {

    @GET("current?")
    fun getCurrentWeather(
        @Query("city") location: String
    ): Deferred<CurrentWeatherResponseWeatherbit>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ) : ApixuWeatherApiService {
            val requestInterceptor = Interceptor{ chain ->

                val url = chain.request()
                    .url()
                    .newBuilder()
                    //.addQueryParameter("access_key", API_KEY)   // add the api key
                    .addQueryParameter("key", API_KEY)
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }


            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                //.baseUrl("http://api.weatherstack.com/")
                .baseUrl("https://api.weatherbit.io/v2.0/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApixuWeatherApiService::class.java)
        }
    }
}