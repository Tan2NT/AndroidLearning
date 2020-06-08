package com.tantnt.android.runstatistic.utils

import android.R.attr
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback
import java.io.File


object Utils {

    fun captureScreenAndShare(id: Long, map : GoogleMap, context: Context) {
        var bitmap : Bitmap? = null
        val callback =
            SnapshotReadyCallback { snapshot ->
                // TODO Auto-generated method stub
               bitmap = snapshot


                val bitmapPath: String =
                    Images.Media.insertImage(context.contentResolver, bitmap, "title", null)
                val bitmapUri = Uri.parse(bitmapPath)
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "image/jpeg"
                intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
                startActivity(context, intent, null)
            }
        map.snapshot(callback)
    }

}