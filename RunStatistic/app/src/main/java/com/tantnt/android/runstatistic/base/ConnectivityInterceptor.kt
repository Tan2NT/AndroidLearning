package com.tantnt.android.runstatistic.base

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response
import java.nio.channels.NotYetConnectedException

class ConnectivityInterceptor ( context: Context) : Interceptor {

    private val appContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isOnline()){
            throw NotYetConnectedException()
        }
        return chain.proceed(chain.request())
    }

    public fun isOnline() : Boolean {
        var connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}