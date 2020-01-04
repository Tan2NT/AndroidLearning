package com.example.intent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    final var TAG : String = "TDebug"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switchToMainActivity()
        Log.i(TAG, "----- onCreate -----")
    }

    private fun switchToMainActivity(){
        btnMainActivity.setOnClickListener(View.OnClickListener {
            var secondIntent : Intent = Intent(this, SecondActivity::class.java)

//            var bundle : Bundle = Bundle()
//            bundle.putString("name", edt_name.text.toString())
//            secondIntent.putExtras(bundle)

            secondIntent.putExtra("name", edt_name.text.toString())
            secondIntent.putExtra("age", 28)
            var subjectList : ArrayList<String> = ArrayList()
            subjectList.add("PHP")
            subjectList.add("Android")
            secondIntent.putStringArrayListExtra("subjects", subjectList)
            startActivity(secondIntent)

        })
    }

    override fun onStart() {
        Log.i(TAG, "----- onStart -----")
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "----- onPause -----")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "----- onStop -----")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "----- onDestroy -----")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "----- onRestart -----")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "----- onResume -----")
    }
}
