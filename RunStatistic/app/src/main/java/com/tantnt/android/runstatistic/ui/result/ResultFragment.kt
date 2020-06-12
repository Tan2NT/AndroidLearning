package com.tantnt.android.runstatistic.ui.result

import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tantnt.android.runstatistic.R
import com.tantnt.android.runstatistic.models.PRACTICE_TYPE
import com.tantnt.android.runstatistic.utils.LOG_TAG
import com.tantnt.android.runstatistic.utils.TimeUtils
import com.tantnt.android.runstatistic.utils.around2Place
import com.tantnt.android.runstatistic.utils.around3Place
import kotlinx.android.synthetic.main.fragment_result.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "image_file_path"
private const val ARG_PARAM2 = "practice_type"
private const val ARG_PARAM3 = "practice_distance"
private const val ARG_PARAM4 = "practice_duration"
private const val ARG_PARAM5 = "practice_speed"
private const val ARG_PARAM6 = "practice_calo"
private const val SHARE_PRACTICE_RESULT_REQUEST_CODE = 50


/**
 * A simple [Fragment] subclass.
 * Use the [ResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var imageFilePath: String? = null
    private var practiceType: Int? = null
    private var practiceDistance: Float? = null
    private var practiceDuration: Long? = null
    private var practiceSpeed: Float? = null
    private var practiceCalo: Float? = null

    private var mBitmap : Bitmap? = null
    private var mBitmapUri : Uri? = null
    private var mShareImagePath : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageFilePath = it.getString(ARG_PARAM1)
            practiceType = it.getInt(ARG_PARAM2)
            practiceDistance = it.getFloat(ARG_PARAM3)
            practiceDuration = it.getLong(ARG_PARAM4)
            practiceSpeed = it.getFloat(ARG_PARAM5)
            practiceCalo = it.getFloat(ARG_PARAM6)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()

        share_btn.setOnClickListener {
             /**
             * Draw practice information into share image
             */
            Log.i(LOG_TAG, "share Button Click")
            sharePractice()
            share_btn.isEnabled = false
        }

        share_btn.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.background.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP)
                    v.invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    v.background.clearColorFilter()
                    v.invalidate()
                }
            }
            false
        }
    }

    fun updateUI() {
        // practice info
        distance_result.text = practiceDistance?.toDouble()?.around2Place().toString()
        calo_result.text = practiceCalo?.toDouble()?.around2Place().toString()
        speed_result.text = practiceSpeed?.toDouble()?.around2Place().toString()
        time_result.text = TimeUtils.convertDutationToFormmated(practiceDuration!!)
        var resId = R.drawable.walking_selected_icon
        when (practiceType) {
            PRACTICE_TYPE.RUNNING.value -> resId = R.drawable.running_selected_icon
            PRACTICE_TYPE.CYCLING.value -> resId = R.drawable.cycling_selected_icon
        }
        practice_type_image.setBackgroundResource(resId)

        mBitmapUri = Uri.parse(imageFilePath)
        mBitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, mBitmapUri)
        mBitmap?.let {
            share_img.setImageBitmap(it)
        }
    }

    private fun sharePractice() {
        mBitmap?.let {
            var paint = Paint()
            paint.style = Paint.Style.FILL
            paint.color = Color.BLACK
            paint.textSize = 50.0f

            // font bold
            val plain = Typeface.createFromAsset(context?.assets, "fonts/COOPBL.TTF")
            val bold = Typeface.create(plain, Typeface.BOLD)
            paint.setTypeface((bold))

            // copy a workign bitmap (inuse on this fragment) to another to adding canvas
            val mutableBitmap: Bitmap =
                it.copy(Bitmap.Config.ARGB_8888, true)

            Log.i(LOG_TAG, "Adding practice info into the bitmap")
            // Try to adding practice info into the bitmap
            val canvas = Canvas(mutableBitmap)

            var practiceTypeString = getString(R.string.practice_type_walking)
            when(practiceType) {
                PRACTICE_TYPE.RUNNING.value -> practiceTypeString = getString(R.string.practice_type_running)
                PRACTICE_TYPE.CYCLING.value -> practiceTypeString = getString(R.string.practice_type_cycling)
            }

            paint.color = Color.BLUE
            // App title
            canvas.drawText(
                getString(R.string.app_name),
                50.0f, 50.0f, paint
            )

            paint.color = Color.BLACK

            // Distance
            canvas.drawText(
                practiceTypeString,
                100.0f, (mutableBitmap.height - 150).toFloat(), paint
            )
            canvas.drawText(
                "${practiceDistance?.toDouble()?.around3Place().toString()} Km ",
                100.0f, (mutableBitmap.height - 80).toFloat(), paint
            )

            // Duration
            canvas.drawText(
                getString(R.string.time),
                (mutableBitmap.width / 2 - 20).toFloat(),
                (mutableBitmap.height - 150).toFloat(),
                paint
            )
            canvas.drawText(
                "${TimeUtils.convertDutationToFormmated(practiceDuration!!)}",
                (mutableBitmap.width / 2 - 30).toFloat(),
                (mutableBitmap.height - 80).toFloat(),
                paint
            )

            // Calo
            canvas.drawText(
                "${getString(R.string.energy).capitalize()}",
                (mutableBitmap.width - 230).toFloat(),
                (mutableBitmap.height - 150).toFloat(),
                paint
            )
            canvas.drawText(
                "${practiceCalo?.toDouble()?.around2Place()} Kcal",
                (mutableBitmap.width - 250).toFloat(),
                (mutableBitmap.height - 80).toFloat(),
                paint
            )

            Log.i(LOG_TAG, "adding canvas done!")

            // Start share intent with image
            mutableBitmap?.let {
                mShareImagePath = MediaStore.Images.Media.insertImage(
                    context?.contentResolver,
                    it,
                    System.currentTimeMillis().toString(),
                    null)
                mShareImagePath?.let { path ->
                    val imageUri = Uri.parse(path)
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "image/jpeg"
                    intent.putExtra(Intent.EXTRA_STREAM, imageUri)
                    startActivityForResult(intent, SHARE_PRACTICE_RESULT_REQUEST_CODE)
                }
            }
        }
    }

    private fun cleanUp() {
        // Delete the image to save user device storage
        imageFilePath?.let {
            try {
                Log.i(LOG_TAG, "ResultActivity -- try to remove image ")
                context?.contentResolver?.delete(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    MediaStore.MediaColumns.DATA + "='" + imageFilePath + "'",
                    null
                )

                context?.contentResolver?.delete(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    MediaStore.MediaColumns.DATA + "='" + mShareImagePath + "'",
                    null
                )
            } catch (e: Exception) {
                Log.e(LOG_TAG, "ResultScreen - fail to delete file $imageFilePath ")
                Log.e(LOG_TAG, "error: $e ")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            SHARE_PRACTICE_RESULT_REQUEST_CODE -> {
                Log.i(LOG_TAG, "Return from share acticity")
            }
        }
    }

    override fun onDestroy() {
        Log.i(LOG_TAG, "ResultActivity -- onDestroy")
        super.onDestroy()
        cleanUp()
    }
}
