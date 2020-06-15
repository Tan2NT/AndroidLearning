package com.tantnt.android.runstatistic.data.network.service

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val WEATHER_API_KEY = "c7ce4a9cd7e64318b1a3aa17f42ba918"
const val WEATHER_BASE_URL = "https://api.weatherbit.io/v2.0/"
//const val WEATHER_BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com"


interface WeatherAPIService {
    @GET("current?")
    fun getCurrentWeatherByCityName(
        @Query("city") location: String
    ): String

    @GET("current?")
    fun getCurrentWeatherByLocation(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ): String

}

object WeatherAPINetwork {

    val requestInterceptor = Interceptor {
        val url = it.request()
            .url()
            .newBuilder()
            .addQueryParameter("key", WEATHER_API_KEY)    // default parameter
            .build()

        val request = it.request()
            .newBuilder()
            .url(url)
            .build()

        return@Interceptor it.proceed(request)
    }


    // @Todo: add logging interceptor & connectivity interceptor
    val loggingInterceptor = HttpLoggingInterceptor()
    //loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(requestInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()


    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(WEATHER_BASE_URL)
        //.addConverterFactory()
        //.addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    val weatherApi = retrofit.create(WeatherAPIService::class.java)

}