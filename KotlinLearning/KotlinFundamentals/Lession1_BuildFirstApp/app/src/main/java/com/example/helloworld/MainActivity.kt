package com.example.helloworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var isNumberGenerated : Boolean = false;

    val MIN_NUMBER : Int = 0
    val MAX_NUMBER : Int = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_rollNumber.setOnClickListener(View.OnClickListener {
            val ranNumber = (MIN_NUMBER..MAX_NUMBER).random()
            txt_randomNumber.text = ranNumber.toString()

            isNumberGenerated = true;
        })

        btn_countUp.setOnClickListener ( View.OnClickListener {

            var newNum : Int

            if(!isNumberGenerated){
                newNum = MIN_NUMBER + 1
            }else
                newNum = txt_randomNumber.text.toString().toInt() + 1

            if(newNum == MAX_NUMBER + 1)
                newNum --

            txt_randomNumber.text = newNum.toString()
        } )
    }
}
