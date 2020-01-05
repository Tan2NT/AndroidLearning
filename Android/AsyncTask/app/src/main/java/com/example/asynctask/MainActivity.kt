package com.example.asynctask

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.URL


class MainActivity : AppCompatActivity() {

    private var TAG : String = "TDebug"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(Build.VERSION.SDK_INT >= 23){
            requestPermissions(arrayOf("android.permission.WRITE_EXTERNAL_STORAGE"), 111)
        }

        btn_Process.setOnClickListener(View.OnClickListener {
//            var task : Task = Task(this)
//            task.execute()

            var dl : DownloadTask = DownloadTask(this)
            dl.execute("https://tse1.mm.bing.net/th?id=OIP.meKOJWc0xJzltV_gYSUMPwHaFj&pid=Api&P=0&w=279&h=210")
        })
    }

    private class DownloadTask(private var activity: MainActivity) : AsyncTask<String, Int, Bitmap>() {

        private var totalSize : Int = 0
        private var downloadedSize : Int = 0

        override fun onPreExecute() {
            totalSize = 0
            downloadedSize = 0
            activity.progressDownload.setProgress(0)

            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): Bitmap? {

            var imageUrl : String? = params[0]
            var bitmap : Bitmap? = null

            try{
                // connect to the image
                var url : URL = URL(imageUrl)
                var urlConnection = url.openConnection()
                urlConnection.connect()

                // get the length
                totalSize = urlConnection.contentLength
                Log.i(activity.TAG, "image size : ${totalSize.toString()}")

                // input stream to read file
                var inputStream = BufferedInputStream(url.openStream(), 8192)

                // OutputStream to write file
                var outputStream : OutputStream = FileOutputStream("downloadedfile.jpg")

                var data : ByteArray = ByteArray(1024)
                var total : Long

                var count = inputStream.read(data)
                while(count != -1){

                    count = inputStream.read(data)

                    if(count == -1)
                        break

                    downloadedSize += count
                    Log.i(activity.TAG, "downloadedSize : ${downloadedSize.toString()}")

                    publishProgress(downloadedSize)

                    outputStream.write(data, 0 , count)
                }

                Log.i(activity.TAG, "22 downloadedSize : ${downloadedSize.toString()}")

//                var baos : ByteArrayOutputStream = outputStream as ByteArrayOutputStream
//                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, baos)
//                val byteArray: ByteArray = baos.toByteArray()
//                bitmap?.recycle()


                //
                outputStream.flush()

                //close
                outputStream.close()
                inputStream.close()



//                //Download bitmap
//                var input : InputStream = java.net.URL(imageUrl).openStream()
//
//
//
//                Log.i(activity.TAG, "URL : ${imageUrl}")
//
//                // Decode bitmap
//                bitmap = BitmapFactory.decodeStream(input)
//
//                Log.i(activity.TAG, "decode done")
            }
            catch (e : Exception){
                Log.i(activity.TAG, e.toString())
            }

            if(bitmap != null)
                Log.i(activity.TAG, "bitmap OK ")
            return bitmap
        }

        override fun onPostExecute(result: Bitmap?) {
            Log.i(activity.TAG, "onPostExecute ------- ")
            // Displaying downloaded image into image view
            // Reading image path from sdcard
            // Displaying downloaded image into image view
// Reading image path from sdcard
            val imagePath: String = "/downloadedfile.jpg"
            // setting downloaded into image view
            // setting downloaded into image view
            activity.img_downloaded.setImageDrawable(Drawable.createFromPath(imagePath))
            //activity.img_downloaded.setImageBitmap(result)
            super.onPostExecute(result)
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
        }
    }

    private class Task(private var activity:MainActivity) : AsyncTask<Void, String, String>(){

        override fun onPreExecute() {
            activity.txt_Info.setText(" start processing -------- ")

            Log.i(activity.TAG, "onPreExecute start processing ---- ")

            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Void?): String {
            Log.i(activity.TAG, "doInBackground - processing")

            for(i in 1..5){
                Thread.sleep(1000)
                publishProgress("task ${i} is done!")
            }

            return "task is Done ----- ";
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onPostExecute(result: String?) {
            Log.i(activity.TAG, "onPostExecute - ${result}")
            activity.txt_Info.setText(result)
            super.onPostExecute(result)
        }

        override fun onProgressUpdate(vararg values: String?) {
            Log.i(activity.TAG, "onPostExecute - ${values[0].toString()}")
            activity.txt_Info.setText(values[0].toString())
            super.onProgressUpdate(*values)
        }

    }

}
