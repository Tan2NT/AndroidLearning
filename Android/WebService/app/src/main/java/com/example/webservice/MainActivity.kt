package com.example.webservice

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

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

        readVolleyArrayJson("http://192.168.1.192/webservice/getdata.php")
    }

    fun readVolleyArrayJson(url : String){
        var requestQueue : RequestQueue = Volley.newRequestQueue(this)
        var jsonArrayRequest = JsonArrayRequest(url,
            Response.Listener { response ->
                loadStudentFromDataBase(response)
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
                Toast.makeText(this, "Error: %s".format(error.toString()), Toast.LENGTH_LONG).show()
            }
        )

        requestQueue.add(jsonArrayRequest)
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

    private fun ShowAddStudentDialog(){
        var dialog : Dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_student)

        var btnAdd : Button = dialog.findViewById(R.id.btn_add_add)
        var btnCancel : Button = dialog.findViewById(R.id.btn_add_cancel)
        var txtName : EditText = dialog.findViewById(R.id.txt_add_name)
        var txtAge : EditText = dialog.findViewById(R.id.txt_add_age)
        var txtAddress : EditText = dialog.findViewById(R.id.txt_add_address)

        btnAdd.setOnClickListener(View.OnClickListener {
            var student = Student(studentList.size + 1, txtName.text.toString(), txtAge.text.toString().toInt(), txtAddress.text.toString())
            dialog.dismiss()
            AddStudent("http://192.168.1.192/webservice/insert.php", student)
        })

        btnCancel.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })

        dialog.show()
    }

    fun AddStudent(url : String, student: Student){
        var requestQueue : RequestQueue = Volley.newRequestQueue(this)
//        var stringRequest : StringRequest = StringRequest(Request.Method.POST, url,
//            Response.Listener<String> { response ->
//                // Display the first 500 characters of the response string.
//                Toast.makeText(this, "Response is: ${response.substring(0, 500)}", Toast.LENGTH_LONG).show()
//            },
//            Response.ErrorListener { Toast.makeText(this, "That didn't work!", Toast.LENGTH_LONG).show()}
//        )
//        stringRequest.

        val jsonobj: JSONObject = JSONObject()

        jsonobj.put("student_name", student.name)
        jsonobj.put("student_year", student.age)
        jsonobj.put("student_address", student.address)

        val que = Volley.newRequestQueue(this)
        val req = JsonObjectRequest(Request.Method.POST,url,jsonobj,
            Response.Listener {
                    response ->
                //println(response["msg"].toString())
                Log.i(TAG, "oooooooooooooookkkkkkkkkkkkkkkkkk")
                readVolleyArrayJson("http://192.168.1.192/webservice/getdata.php")
            }, Response.ErrorListener {error ->
                Log.i(TAG, "Error ${error.message}")
            }
        )
        que.add(req)

        requestQueue.add(req)

    }

    fun loadStudentFromDataBase(jsonArray: JSONArray){
        studentList.clear()
        for(i in 0..jsonArray.length()-1){
            var jObj = jsonArray.get(i) as JSONObject
            var id : Int = jObj.get("id").toString().toInt()
            var name : String  = jObj.get("name").toString()
            var age : Int = jObj.get("yearOfBird").toString().toInt()
            var address : String = jObj.get("address").toString()
            Log.i(TAG, "${id} - ${name}")
            studentList.add(Student(id, name, age, address))
        }

        studentAdapter = StudentAdapter(this, R.layout.student_list_detail, studentList)
        lv_students.adapter = studentAdapter
    }
}
