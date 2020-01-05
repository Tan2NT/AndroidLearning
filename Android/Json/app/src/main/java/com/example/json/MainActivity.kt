package com.example.json

import android.app.DownloadManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.URL

class MainActivity : AppCompatActivity() {

    var viJson : JSONObject? = null
    var enJson : JSONObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //volleyStringRequest()

        volleyJsonRequest()

        var json = ReadJson(this)
        //json.execute("https://khoapham.vn/KhoaPhamTraining/json/tien/demo1.json")
        //json.execute("https://khoapham.vn/KhoaPhamTraining/json/tien/demo2.json")
        //json.execute("https://khoapham.vn/KhoaPhamTraining/json/tien/demo3.json")
        json.execute("https://khoapham.vn/KhoaPhamTraining/json/tien/demo4.json")


        img_ViFlag.setOnClickListener(View.OnClickListener {
            displayInfo(viJson as JSONObject)
        })

        img_EnFlag.setOnClickListener(View.OnClickListener {
            displayInfo(enJson as JSONObject)
        })

    }

    fun volleyJsonRequest(){
        var requestQueue = Volley.newRequestQueue(this)
        var url = "https://khoapham.vn/KhoaPhamTraining/json/tien/demo1.json"

        //val url = "http://my-json-feed"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                processJsonObject(response.toString())
                Toast.makeText(this, "Response: %s".format(response.toString()), Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
                Toast.makeText(this, "Error: %s".format(error.toString()), Toast.LENGTH_LONG).show()
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    fun volleyStringRequest(){
        val requestQueue = Volley.newRequestQueue(this)
        val url = "http://online.khoapham.vn/"
        val stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                Toast.makeText(this, "Response is: ${response.substring(0, 500)}", Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener { Toast.makeText(this, "That didn't work!", Toast.LENGTH_LONG).show()})
        requestQueue.add(stringRequest)
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

    public fun processJsonLanguage(s : String){
        try{
            var jLang = JSONObject(s).getJSONObject("language")
            viJson = jLang.getJSONObject("vn")
            enJson = jLang.getJSONObject("en")
        }catch(e : Exception){
            Log.i("TDebug", "processJsonLanguage " + e.toString())
        }
    }

    fun displayInfo(jobj : JSONObject){

        if(jobj == null)
        {
            txt_info.setText("")
            return
        }

        var name : String = jobj.get("name").toString()
        var address : String = jobj.get("address").toString()
        var course1 : String = jobj.get("course1").toString()
        var course2 : String = jobj.get("course2").toString()
        var course3 : String = jobj.get("course3").toString()

        var info = ""
        info += name + "\n"
        info += address + "\n"
        info += course1 + "\n"
        info += course2 + "\n"
        info += course3 + "\n"
        txt_info.setText(info)
    }

    public fun processJsonArrayObject(s : String){
        try {
            var jArray = JSONArray(s)
            Log.i("TDebg", "array size is ${jArray.length()}")
            for(i in 0..jArray.length()-1){
                var jObj = jArray.getJSONObject(i)
                var monhoc = jObj.get("khoahoc").toString()
                var hocphi = jObj.get("hocphi").toString()
                Log.i("TDebug", "${monhoc} - ${hocphi}")
            }
        }catch(e: Exception){
            Log.i("TDebug", "processJsonArrayObject " + e.toString())
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
                   Log.i("Tdebug", line)
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

            // process Array Json
            //activity.processJsonArray(result as String)

            // process language Json
            //activity.processJsonLanguage(result as String)

            //process Json Array Object
            activity.processJsonArrayObject(result as String)


            super.onPostExecute(result)
        }

    }


}
