package com.tanhoanngoc.glofttest.listfragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_student_detail.*

class StudentDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_detail)

        if(intent != null){
            var student = intent.getSerializableExtra("student") as Student
            if(student != null){
                txt_detailName.setText(student.name)
                txt_detailAddress.setText("Address: " + student.address)
                txt_detailAge.setText("Age: " + student.age.toString())
            }
        }
    }
}
