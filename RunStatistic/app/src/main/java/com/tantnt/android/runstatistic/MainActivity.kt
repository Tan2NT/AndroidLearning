package com.tantnt.android.runstatistic

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tantnt.android.runstatistic.utils.LOG_TAG

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        // find the navigation controler
        navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_practice, R.id.navigation_profile))


        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        AndroidThreeTen.init(this.application)

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    override fun onDestroy() {
        Log.i(LOG_TAG, "MainActivity - onDestroy")
        super.onDestroy()


    }
}
