package com.tantnt.android.runstatistic.utils

import android.R.attr
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.*
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
                    paint.textSize = 50.0f

                    // font bold
                    val plain = Typeface.createFromAsset(context.assets, "fonts/COOPBL.TTF")
                    val bold = Typeface.create(plain, Typeface.BOLD)
                    paint.setTypeface((bold))

                    // Try to adding practice info into the bitmap
                    val canvas = Canvas(bitmap!!)

                    // Distance
                    canvas.drawText("${practive.getTypeString().capitalize()}",
                        100.0f, (bitmap!!.height - 150).toFloat(), paint)
                    canvas.drawText("${practive.distance.around3Place().toString()} Km ",
                        100.0f, (bitmap!!.height - 80).toFloat(), paint)

                    // Duration
                    canvas.drawText("TIME",
                        (bitmap!!.width/2 - 20).toFloat(), (bitmap!!.height - 150).toFloat(), paint)
                    canvas.drawText("${TimeUtils.convertDutationToFormmated(practive.duration.toLong())}",
                        (bitmap!!.width/2 - 30).toFloat(), (bitmap!!.height - 80).toFloat(), paint)

                    // Calo
                    canvas.drawText("${context.getString(R.string.calo_text).capitalize()}",
                        (bitmap!!.width - 230).toFloat(), (bitmap!!.height - 150).toFloat(), paint)
                    canvas.drawText("${practive.calo} Kcal",
                        (bitmap!!.width - 250).toFloat(), (bitmap!!.height - 80).toFloat(), paint)

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