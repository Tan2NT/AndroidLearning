package com.example.intent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        switchToMainActivity()
    }

    private fun switchToMainActivity(){
        btnSecondActivity.setOnClickListener(View.OnClickListener {
            var mainItent : Intent = Intent(this, MainActivity::class.java)
            startActivity(mainItent)
        })
    }
}
