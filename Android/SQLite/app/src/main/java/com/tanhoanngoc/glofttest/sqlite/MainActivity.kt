package com.tanhoanngoc.glofttest.sqlite

import android.app.Dialog
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG : String = "TDebug"

    companion object{
        var database : Database? = null
    }

    var studentList : ArrayList<Student> = ArrayList()
    var studentAdapter : StudentAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // create database
        database = Database(this, "student_database.sqlite", null, 1)

        // create table Student
        database?.QueryData("CREATE TABLE IF NOT EXISTS Student(Id INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR(30), Age INTEGER)")

        // insert data
        //database.QueryData("INSERT INTO Student VALUES(null, 'Nguyen Ngoc Tri', 27)")

        // get data
        loadStudentFromDataBase(database!!)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_student, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menuAddStudent){
            ShowAddStudentDialog()
        }

        return super.onOptionsItemSelected(item)
    }

    fun addStudentToDatabase(database: Database, student: Student){
        database.QueryData("INSERT INTO Student VALUES(null, '${student.name}', ${student.age})")
    }

    fun updateStudentToDatabase(database: Database, student: Student){
        database.QueryData("UPDATE Student set Name = '${student.name}', Age = ${student.age} WHERE Id = ${student.id}")
    }

    fun loadStudentFromDataBase(database : Database){

        var students : Cursor = database.getData("SELECT * FROM Student")
        while(students.moveToNext()){
            var id = students.getInt(0)
            var name = students.getString(1)
            var age = students.getInt(2)
            Log.i(TAG, "${id} - ${name} - ${age}")
            studentList.add(Student(id, name, age))
        }

        studentAdapter = StudentAdapter(this, R.layout.student_list_detail, studentList)
        lv_students.adapter =studentAdapter
    }

    private fun showEditDialog(database: Database, student: Student, pos : Int){
        var dialog : Dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_edit_student)

        var btnUpdate : Button = dialog.findViewById(R.id.btnUpdateStudent)
        var btnCancel : Button = dialog.findViewById(R.id.btnEditCancel)
        var txtName : EditText = dialog.findViewById(R.id.txtEditStudentName)
        var txtAge : EditText = dialog.findViewById(R.id.txtEditStudentAge)

        txtAge.setText(student.age.toString())
        txtName.setText(student.name)

        btnUpdate.setOnClickListener(View.OnClickListener {
            student.name = txtName.text.toString()
            student.age = txtAge.text.toString().toInt()
            updateStudentToDatabase(database, student)
            studentList[pos] = student
            studentAdapter?.notifyDataSetChanged()
            dialog.dismiss()
        })

        btnCancel.setOnClickListener(View.OnClickListener { dialog.dismiss() })
    }

    private fun ShowAddStudentDialog(){
        var dialog : Dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_student)

        var btnAdd : Button = dialog.findViewById(R.id.btnAddStudent)
        var btnCancel : Button = dialog.findViewById(R.id.btnCancel)
        var txtName : EditText = dialog.findViewById(R.id.txtStudentName)
        var txtAge : EditText = dialog.findViewById(R.id.txtStudentAge)

        btnAdd.setOnClickListener(View.OnClickListener {
            var student = Student(studentList.size + 1, txtName.text.toString(), txtAge.text.toString().toInt())
            studentList.add(student)
            addStudentToDatabase(database!!, student)
            studentAdapter?.notifyDataSetChanged()
            dialog.dismiss()
        })

        btnCancel.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })

        dialog.show()
    }
}
