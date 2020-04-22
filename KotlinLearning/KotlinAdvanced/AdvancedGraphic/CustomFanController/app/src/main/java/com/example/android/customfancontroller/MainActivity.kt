package com.example.android.customfancontroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "MainActivity.onCreate 11")
        setContentView(R.layout.activity_main)
        Log.i(TAG, "MainActivity.onCreate 22")
    }
}
