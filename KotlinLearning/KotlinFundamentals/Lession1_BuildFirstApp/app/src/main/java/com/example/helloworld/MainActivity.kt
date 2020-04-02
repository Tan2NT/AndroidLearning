package com.example.helloworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val MIN_NUMBER : Int = 1
    val MAX_NUMBER : Int = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_rollNumber.setOnClickListener(View.OnClickListener {
            val ranNumber = (MIN_NUMBER..MAX_NUMBER).random()

            val imageResourceId = when (ranNumber){
                1 -> R.drawable.dice_1
                2 -> R.drawable.dice_2
                3 -> R.drawable.dice_3
                4 -> R.drawable.dice_4
                5 -> R.drawable.dice_5
                else -> R.drawable.dice_6
            }

            img_diceImage.setImageResource(imageResourceId)
        })
    }
}
