package com.tanhoanngoc.glofttest.sqlite

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_item.*


class AddItemActivity : AppCompatActivity() {

    var CAMERA_REQUEST_CODE = 100
    var MY_CAMERA_PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        img_takePhoto.setOnClickListener(View.OnClickListener {
            if(Build.VERSION.SDK_INT >= 23){
                if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                        arrayOf(android.Manifest.permission.CAMERA),
                        MY_CAMERA_PERMISSION_CODE
                    )
                } else {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
                }
            }else{
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show()
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    var bitmap = data.extras?.get("data") as Bitmap
                    imag_itemPic.setImageBitmap(bitmap)
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
