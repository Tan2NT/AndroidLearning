package com.tanhoanngoc.glofttest.listfragment

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.ListFragment

class FragmentListStudent : ListFragment(){

    var studentList : ArrayList<Student> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        initStudentList()

        listAdapter = StudentAdapter(activity as Context, R.layout.row_student_detail, studentList)

        return inflater.inflate(R.layout.fragment_list_student, container, false)
    }

    fun initStudentList(){
        studentList.add(Student("Hoang Ngoc Tan", "Phong Dien, Thua Thien Hue", 28))
        studentList.add(Student("Nguyen Ngoc Tri", "Huong Tra, Thua Thien Hue", 28))
        studentList.add(Student("Tran Thanh Phong", "Lien Chieu, Da Nang", 25))
        studentList.add(Student("Tran Tu Hoang", "Chu Se, DakLak", 31))
        studentList.add(Student("Tra Giang", "Dai Loc, Quang Nam", 23))
        studentList.add(Student("Nguyen Duc Thuan", "Dien Chau, Nghe An", 27))
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        var student : Student = studentList.get(position)
        val orientation = resources.configuration.orientation

        if(activity?.supportFragmentManager?.findFragmentById(R.id.fgStudentDetailLand) != null && orientation == Configuration.ORIENTATION_LANDSCAPE){
            var txtName : TextView = activity?.findViewById(R.id.txt_detailName) as TextView
            var txtAddress : TextView = activity?.findViewById(R.id.txt_detailAddress) as TextView
            var txtAge : TextView = activity?.findViewById(R.id.txt_detailAge) as TextView

            txtAddress.setText("Address: " + student.address)
            txtName.setText(student.name)
            txtAge.setText("Age: " + student.age.toString())
        }else{
            var detailIntent = Intent()
            detailIntent.setClass(activity as Context, StudentDetailActivity::class.java)
            detailIntent.putExtra("student", student)
            startActivity(detailIntent)
        }

        super.onListItemClick(l, v, position, id)
    }
}