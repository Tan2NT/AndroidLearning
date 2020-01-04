package com.example.intent

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_second.*
import java.util.ArrayList

class SecondActivity : AppCompatActivity() {

    final var TAKE_PICTURE_REQUEST_CODE : Int = 100
    final var REQUEST_CAMERA_PERMISSION : Int = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

//        var name : String = intent?.getStringExtra("name").toString()
//        var age : Int? = intent?.getIntExtra("age", 1234)
//        var subjects : ArrayList<String> = intent.getStringArrayListExtra("subjects")
//        var student : Student = intent.getSerializableExtra("student") as Student

        var bundle : Bundle = intent.getBundleExtra("data")

        if(bundle != null){
            var student : Student = bundle.getSerializable("student") as Student
            var subjects : ArrayList<String> = bundle.getStringArrayList("subjects") as ArrayList<String>

            txt_hello_friend.setText("Hello ${student.name}, he's ${student.name} age")

            var message : String = "your registed subjects are ";

            for(i in 0 .. subjects.size - 1){
                message = message + subjects.get(i).toString() + " "
            }

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        //switchToMainActivity()

        btnTakePhoto.setOnClickListener(View.OnClickListener {
            if(checkPermission(android.Manifest.permission.CAMERA)){
                takePhoto()
            }else{
                requestPermission(android.Manifest.permission.CAMERA, REQUEST_CAMERA_PERMISSION)
            }

        })
    }

    fun checkPermission(pemissionName : String): Boolean{
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            return true
        }else
        {
            Log.i("TDebug", "permission ${pemissionName} is not granted")
            return false
        }
    }

    fun requestPermission(permisionName : String, requestCode: Int){
        if(!checkPermission(permisionName))
        {
            ActivityCompat.requestPermissions(this, arrayOf(permisionName), requestCode)
        }
    }

    fun takePhoto(){
        var cameraIntent : Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, TAKE_PICTURE_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CAMERA_PERMISSION -> {
                if(!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    takePhoto()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == TAKE_PICTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null){
            var bitmap : Bitmap = data.extras?.get("data") as Bitmap
            imgCameraPhoto.setImageBitmap(bitmap)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun switchToMainActivity(){
        btnSecondActivity.setOnClickListener(View.OnClickListener {
//            var mainItent : Intent = Intent(this, MainActivity::class.java)
//            startActivity(mainItent)

            // Activity result
            var message = "The student info is displayed succeed !"
            var resultIntent : Intent = Intent()
            resultIntent.putExtra("message", message)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()

        })
    }
}
