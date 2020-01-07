package com.tanhoanngoc.glofttest.sqlite

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class StudentAdapter (var context : Context, var layoutID : Int, var studentList: ArrayList<Student>) : BaseAdapter(){

    class ViewHolder(view : View){
        var txtName : TextView = view.findViewById(R.id.txt_name)
        var txtId : TextView = view.findViewById(R.id.txt_itemDetail_name)
        var txtAge : TextView = view.findViewById(R.id.txt_itemDetail_Des)
        var btnEdit : ImageButton = view.findViewById(R.id.btn_itemDetail_Edit)
        var btnDelete : ImageButton = view.findViewById(R.id.btn_itemDetail_Delete)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder : ViewHolder
        var view : View
        if(convertView == null){
            var inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(layoutID, null)

            holder = ViewHolder(view)
            view.setTag(holder)
        }else{
            view = convertView
            holder = view.tag as ViewHolder
        }

        var student = studentList.get(position)
        holder.txtName.setText(student.name)
        holder.txtId.setText("ID: " + student.id.toString())
        holder.txtAge.setText("Age: " + student.age.toString())

        holder.btnEdit.setOnClickListener(View.OnClickListener {

            var dialog : Dialog = Dialog(context)
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
                MainActivity.database?.QueryData("UPDATE Student set Name = '${student.name}', Age = ${student.age} WHERE Id = ${student.id}")
                studentList[position] = student
                notifyDataSetChanged()
                dialog.dismiss()
            })

            btnCancel.setOnClickListener(View.OnClickListener { dialog.dismiss() })

            dialog.show()
        })

        holder.btnDelete.setOnClickListener(View.OnClickListener {
            MainActivity.database?.QueryData("DELETE FROM Student WHERE Id = ${student.id}")
            studentList.removeAt(position)
            notifyDataSetChanged()
        })


        return view

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(position: Int): Any {
        return studentList.get(position)
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemId(position: Int): Long {
        return 0
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        return studentList.size
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}