package com.example.json

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import org.json.JSONArray
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
        //json.execute("https://khoapham.vn/KhoaPhamTraining/json/tien/demo1.json")
        json.execute("https://khoapham.vn/KhoaPhamTraining/json/tien/demo2.json")

    }

    public fun  processJsonObject(s : String){
        try {
            var jobj : JSONObject = JSONObject(s)
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
    }

    public fun processJsonArray(s : String){
        try {
            var jobj : JSONObject = JSONObject(s)
            var jArray : JSONArray = jobj.getJSONArray("danhsach")
            for(i in 0..jArray.length() - 1){
                var jObject = jArray.getJSONObject(i)
                var khoahoc : String = jObject.get("khoahoc").toString()
                Log.i("TDebug", "${khoahoc}")
            }

        }catch(e : Exception){
            Log.i("TDebug", "processJsonArray " + e.toString())
        }
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
            // process Json Object
            //activity.processJsonObject(result as String)

            activity.processJsonArray(result as String)


            super.onPostExecute(result)
        }

    }


}
