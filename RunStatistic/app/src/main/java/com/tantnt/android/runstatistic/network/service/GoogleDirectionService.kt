package com.tantnt.android.runstatistic.network.service

import android.location.Location
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/*
A retrofit service to fetch a google direction
 */

/*
https://maps.googleapis.com/maps/api/directions/json?
origin=Toronto&destination=Montreal
&key=AIzaSyAX38GQpUkkUpjQuOX1Tz1pq0opqDThQiM
 */

const val GOOGLE_DIRECTIONS_API_KEY = "AIzaSyAX38GQpUkkUpjQuOX1Tz1pq0opqDThQiM"
const val GOGGLE_DIRECTIONS_BASE_URL = "https://maps.googleapis.com/maps/api/directions/"
const val GOOGLE_DIRECTIONS_OUTPUT_FORMAT = "json"
const val TAG : String = "TDebug"

interface GoogleDirectionsService {
    @GET("json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination : String
    )  : String
}

/*
Main entry point for network access. Call like 'GoogleDirectionsNetwork.googleDirections.getDirections()
 */
object GoogleDirectionsNetwork {

    val requestInterceptor = Interceptor {
        val url = it.request()
            .url()
            .newBuilder()
            .addQueryParameter("key", GOOGLE_DIRECTIONS_API_KEY)    // default parameter
            .addQueryParameter("sensor", "false")    // default parameter
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

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit : Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(GOGGLE_DIRECTIONS_BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        //.addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val googleDirections : GoogleDirectionsService = retrofit.create(GoogleDirectionsService::class.java)
}