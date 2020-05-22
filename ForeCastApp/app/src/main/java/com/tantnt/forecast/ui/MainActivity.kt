package com.tantnt.forecast.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.tantnt.forecast.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

private val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein by closestKodein()
    private val fusedLocationProviderClient : FusedLocationProviderClient by instance()
    private val TAG : String = "TDebug"

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
        }
    }

    private lateinit var navController: NavController

    companion object{

        private lateinit var mContext: Context
        fun setAppContext(context: Context) {
            mContext = context
        }

        fun hasLocationPermission(): Boolean {
            return ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setAppContext(this)

        // find the navigation controler
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        // connect the navigation button to the controller
        bottom_nav.setupWithNavController(navController)

        NavigationUI.setupActionBarWithNavController(this, navController)

        requestLocationPermission()

        if(hasLocationPermission()){
            Log.d(TAG, "onCreate - Has Location permission")
            bindLocationManager()
        }else{
            Log.d(TAG, "onCreate - Doesn't has Location permission => Request")
            requestLocationPermission()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(null, navController)
    }

    private fun requestLocationPermission(){
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                MY_PERMISSION_ACCESS_COARSE_LOCATION
                )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == MY_PERMISSION_ACCESS_COARSE_LOCATION){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d(TAG, "onRequestPermissionResult - location - succeed")
                bindLocationManager()
                bottom_nav.selectedItemId = R.id.currentWeatherFragment

            }else {
                Log.d(TAG, "onRequestPermissionResult - Location - failed")
                Toast.makeText(this, "Please, set location manually in setting", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun bindLocationManager(){
        Log.d(TAG, "BindLocationManager - start")
        LifecycleBoundLocationManager(this, fusedLocationProviderClient, locationCallback)
        Log.d(TAG, "BindLocationManager - done")
    }
}
