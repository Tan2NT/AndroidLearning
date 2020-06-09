package com.tantnt.android.runstatistic.utils

import android.R.attr
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback
import com.tantnt.android.runstatistic.R
import com.tantnt.android.runstatistic.models.PracticeModel
import kotlinx.android.synthetic.main.fragment_detail.view.*
import java.io.File


object Utils {

    fun captureScreenAndShare(practive: PracticeModel, map : GoogleMap, context: Context) {
        var bitmap : Bitmap? = null
        val callback =
            SnapshotReadyCallback { snapshot ->
                // TODO Auto-generated method stub
               bitmap = snapshot

                bitmap?.let {

                    var paint = Paint()
                    paint.style = Paint.Style.FILL
                    paint.color = Color.BLACK
                    paint.textSize = 20.0f

                    // Try to adding practice info into the bitmap
                    val canvas = Canvas()
                    bitmap?.width?.minus(100.0f)?.let { it1 ->
                        canvas.drawText("${context.getString(R.string.time_min)} : ${TimeUtils.convertDutationToFormmated(practive.duration.toLong())}",
                            it1,
                            it1, paint)
                    }
                    canvas.drawText("${context.getString(R.string.distance_km)} : ${practive.distance}",
                        (bitmap!!.width + 10).toFloat(), (bitmap!!.height/4).toFloat(), paint)

                    canvas.drawText("${context.getString(R.string.time_min)} : ${TimeUtils.convertDutationToFormmated(practive.duration.toLong())}",
                        (bitmap!!.width/2).toFloat(), (bitmap!!.height/4).toFloat(), paint)

                    canvas.drawText("${context.getString(R.string.calo_text)} : ${practive.calo}",
                        (bitmap!!.width - 100).toFloat(), (bitmap!!.height/4).toFloat(), paint)

                    // Share result to others
                    val bitmapPath: String =
                        Images.Media.insertImage(context.contentResolver, bitmap, practive.startTime.toString(), null)

                    val bitmapUri = Uri.parse(bitmapPath)
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "image/jpeg"
                    intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
                    startActivity(context, intent, null)
                }
            }
        map.snapshot(callback)
    }

}