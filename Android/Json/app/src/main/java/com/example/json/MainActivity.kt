package com.example.json

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var json = ReadJson(this)
        json.execute("https://khoapham.vn/KhoaPhamTraining/json/tien/demo1.json")

    }

    private class ReadJson(var activity: MainActivity) : AsyncTask<String, Void, String>(){
        override fun doInBackground(vararg params: String?): String {
            var url = URL(params[0])

            var inputStreamReader : InputStreamReader = InputStreamReader(url.openConnection().getInputStream())
            var bfReader : BufferedReader = BufferedReader(inputStreamReader)

            var builder : StringBuilder = StringBuilder()

            while (true){
               try {
                   var line = bfReader.readLine()
                   if(line == null)
                       break

                   Log.i("TDebug", "${line}")
                   builder.append(line)
               }catch (e : Exception){
                Log.i("Tdebug", "An Error has occurred" + e.toString())
            }

            }

            bfReader.close()

            return builder.toString()
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onPostExecute(result: String?) {
            try {
                var jobj : JSONObject = JSONObject(result)
                var monhoc : String = jobj.get("monhoc").toString()
                var noihoc : String = jobj.get("noihoc").toString()
                var website : String = jobj.get("website").toString()
                var logo : String = jobj.get("logo").toString()
                var fanpage : String = jobj.get("fanpage").toString()

                Log.i("TDebug", "${monhoc}")
                Log.i("TDebug", "${noihoc}")
                Log.i("TDebug", "${website}")
                Log.i("TDebug", "${logo}")
                Log.i("TDebug", "${fanpage}")

            }catch(e : Exception){

            }

            super.onPostExecute(result)
        }

    }


}
