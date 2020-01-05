package com.example.asynctask

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.InputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private var TAG : String = "TDebug"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_Process.setOnClickListener(View.OnClickListener {
//            var task : Task = Task(this)
//            task.execute()

            var dl : DownloadTask = DownloadTask(this)
            dl.execute("https://tse1.mm.bing.net/th?id=OIP.meKOJWc0xJzltV_gYSUMPwHaFj&pid=Api&P=0&w=279&h=210")
        })
    }

    private class DownloadTask(private var activity: MainActivity) : AsyncTask<String, Int, Bitmap>() {

        private var totalSize : Int = 0

        override fun onPreExecute() {
            activity.progressDownloaded.setProgress(0)

            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): Bitmap? {

            var imageUrl : String? = params[0]
            var bitmap : Bitmap? = null

            try{
                //Download bitmap
                var input : InputStream = java.net.URL(imageUrl).openStream()

                //totalSize = input.read()

                Log.i(activity.TAG, "URL : ${imageUrl}")

                // Decode bitmap
                bitmap = BitmapFactory.decodeStream(input)

                Log.i(activity.TAG, "decode done")
            }
            catch (e : Exception){
                Log.i(activity.TAG, e.toString())
            }

            if(bitmap != null)
                Log.i(activity.TAG, "bitmap OK ")
            return bitmap
        }

        override fun onPostExecute(result: Bitmap?) {
            activity.img_downloaded.setImageBitmap(result)
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
