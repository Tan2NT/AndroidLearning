package com.example.intent

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable
import java.lang.Exception
import java.security.Permission
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    final var TAG : String = "TDebug"
    final var CALL_PHONE_PERMISSION_REQUEST_CODE = 111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switchToMainActivity()

        Log.i(TAG, "----- onCreate -----")

        // ACTION_VIEW
        imgBrowser.setOnClickListener(View.OnClickListener {
            var browserIntent : Intent = Intent( Intent.ACTION_VIEW)
            browserIntent.setData(Uri.parse("https://www.facebook.com"))
            startActivity(browserIntent)
        })

        // ACTION_SENDTO
        btnSendMessage.setOnClickListener(View.OnClickListener {
            var messageIntent : Intent = Intent()
            messageIntent.setAction(Intent.ACTION_SENDTO)
            //messageIntent.putExtra("phone_number", edt_phoneNumber.text.toString())
            messageIntent.setData(Uri.parse("sms:" + edt_phoneNumber.text.toString()))
            messageIntent.putExtra("sms_body", edt_message.text.toString())

            startActivity(messageIntent)
        })

        //ACTION_CALL
        btnMakeCall.setOnClickListener(View.OnClickListener {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.CALL_PHONE)) {
                    var builder : AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setMessage("permission call phone is required to making this action")
                    builder.setTitle("Permission required")
                    builder.setPositiveButton("OK") {
                        dialog, which ->
                        makeRequestPersmission(android.Manifest.permission.CALL_PHONE)
                        val dialog = builder.create()
                        dialog.show()
                    }
                } else {
                    makeRequestPersmission(android.Manifest.permission.CALL_PHONE)
                }
            } else {
                // Permission has already been granted
                makeCall()
            }
        })
    }

    fun makeCall(){
        try {
            var callItent : Intent = Intent()
            callItent.setAction(Intent.ACTION_CALL)
            callItent.setData(Uri.parse("tel:0384992090"))
            startActivity(callItent)
        }catch (e : Exception){
            Log.e(TAG, e.toString())
        }
    }

    fun makeRequestPersmission(permissionName : String){
        ActivityCompat.requestPermissions(this, arrayOf(permissionName), CALL_PHONE_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            CALL_PHONE_PERMISSION_REQUEST_CODE -> {
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Log.w(TAG, "Permission ${permissions[0].toString()} is denied by user")
                }else{
                    Log.i(TAG, "permission ${permissions[0]} is granted")
                    makeCall()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun switchToMainActivity(){
        btnMainActivity.setOnClickListener(View.OnClickListener {
            var secondIntent : Intent = Intent(this, SecondActivity::class.java)

            secondIntent.putExtra("name", edt_name.text.toString())
            secondIntent.putExtra("age", 28)

            // send an array
            var subjectList : ArrayList<String> = ArrayList()
            subjectList.add("PHP")
            subjectList.add("Android")
            secondIntent.putStringArrayListExtra("subjects", subjectList)

            // send object
            var student : Student = Student("Tan", 28)
            secondIntent.putExtra("student", student as Serializable)

            // send bundle
            var bundle : Bundle = Bundle()
            bundle.putInt("id", 1234)
            bundle.putStringArrayList("subjects", subjectList)
            bundle.putSerializable("student", student)
            secondIntent.putExtra("data", bundle)

            startActivity(secondIntent)

        })
    }

    override fun onStart() {
        Log.i(TAG, "----- onStart -----")
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "----- onPause -----")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "----- onStop -----")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "----- onDestroy -----")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "----- onRestart -----")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "----- onResume -----")
    }
}
