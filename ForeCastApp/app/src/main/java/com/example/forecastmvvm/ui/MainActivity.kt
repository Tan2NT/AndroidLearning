package com.example.forecastmvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.forecastmvvm.R
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // find the navigation controler
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        // connect the navigation button to the controler
        bottom_nav.setupWithNavController(navController)

        try{
            // setup the action bar with navigation controler
            NavigationUI.setupActionBarWithNavController(this, navController)
        }
        catch (e: Exception){
            Log.i("Tdebug", "err: " + e.toString())
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(null, navController)
    }
}
