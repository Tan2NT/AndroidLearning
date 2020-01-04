package com.example.intent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_second.*
import java.util.ArrayList

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)


        var name : String = intent?.getStringExtra("name").toString()
        var age : Int? = intent?.getIntExtra("age", 1234)
        var subjects : ArrayList<String> = intent.getStringArrayListExtra("subjects")
        var student : Student = intent.getSerializableExtra("student") as Student

        txt_hello_friend.setText("Hello ${student.name}, he's ${student.name} age")

        var message : String = "your registed subjects are ";

        for(i in 0 .. subjects.size - 1){
            message = message + subjects.get(i).toString() + " "
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        switchToMainActivity()
    }

    private fun switchToMainActivity(){
        btnSecondActivity.setOnClickListener(View.OnClickListener {
            var mainItent : Intent = Intent(this, MainActivity::class.java)
            startActivity(mainItent)
        })
    }
}
