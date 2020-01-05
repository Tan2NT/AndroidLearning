package com.example.intentexcercise

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.gridview_animal_detail.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    final var CHOSE_ANIMAL_REQUEST_CODE : Int = 100

    //private var mScore : SharedPreferences = getSharedPreferences("gameScore", Context.MODE_PRIVATE)
    private var mMainPicId : Int = -1
    private var mAnimalList : Array<Int> = arrayOf(R.drawable.bird, R.drawable.cat, R.drawable.dog,
        R.drawable.elephant, R.drawable.mouth, R.drawable.tiger, R.drawable.squirrel)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        autoGenerateAnimal()

        img_mainAnimal.setOnClickListener(View.OnClickListener {
            autoGenerateAnimal()
        })

        img_refresh.setOnClickListener(View.OnClickListener {
            autoGenerateAnimal()
        })

        var ranPos : Int = Random.nextInt(mAnimalList.size)
        img_yourAnimal.setImageResource(mAnimalList[ranPos])

        img_yourAnimal.setOnClickListener(View.OnClickListener {
            var animalIntent : Intent = Intent()
            animalIntent.setClass(this, AnimalPickerActivity::class.java)
            startActivityForResult(animalIntent, CHOSE_ANIMAL_REQUEST_CODE)
        })

        updateScoreText()
    }

    private fun autoGenerateAnimal(){
        mMainPicId = Random.nextInt(mAnimalList.size)
        img_mainAnimal.setImageResource(mAnimalList[mMainPicId])
    }

    private fun updateScoreText(){
        var score : Int = getScore()
        txtScore.setText("SCORE: " + score.toString())
    }

    private fun getScore() : Int {
        var mScore = getSharedPreferences("gameScore", Context.MODE_PRIVATE)
        return mScore.getInt("currentScore", 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            CHOSE_ANIMAL_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK){
                    var picID : Int? = data?.getIntExtra("picId", -1)

                    if(picID != -1)
                        picID?.let { img_yourAnimal.setImageResource(it) }

                    if(mAnimalList[mMainPicId] == picID)
                    {
                        var mScore = getSharedPreferences("gameScore", Context.MODE_PRIVATE)
                        var currentScore = getScore()
                        currentScore += 1
                        var editor : SharedPreferences.Editor =  mScore.edit()
                        editor.putInt("currentScore", currentScore)
                        editor.commit()

                        updateScoreText()

                        Toast.makeText(this, "Congratulation! Good job .. Your Score: ${currentScore}", Toast.LENGTH_LONG).show()
                    }
                    else
                        Toast.makeText(this, "Ops!  try again later", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
