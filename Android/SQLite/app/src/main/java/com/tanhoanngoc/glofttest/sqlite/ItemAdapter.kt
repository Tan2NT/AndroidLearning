package com.tanhoanngoc.glofttest.sqlite

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.io.ByteArrayOutputStream


class ItemAdapter (var context : Context, var layoutID : Int, var itemList: ArrayList<Item>) : BaseAdapter(){

    class ViewHolder(view : View){
        var txtName : TextView = view.findViewById(R.id.txt_itemDetail_name)
        var txtDetail : TextView = view.findViewById(R.id.txt_itemDetail_Des)
        var btnEdit : ImageButton = view.findViewById(R.id.btn_itemDetail_Edit)
        var btnDelete : ImageButton = view.findViewById(R.id.btn_itemDetail_Delete)
        var imgPic : ImageView = view.findViewById(R.id.img_itemDetail_pic)
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

        var item = itemList.get(position)
        holder.txtName.setText(item.name)
        holder.txtDetail.setText("ID: " + item.detail)

        holder.imgPic.setImageBitmap(BitmapFactory.decodeByteArray(item.imageData, 0, item.imageData.size))

//        holder.btnEdit.setOnClickListener(View.OnClickListener {
//
//            var dialog : Dialog = Dialog(context)
//            dialog.setContentView(R.layout.dialog_edit_student)
//
//            var btnUpdate : Button = dialog.findViewById(R.id.btnUpdateStudent)
//            var btnCancel : Button = dialog.findViewById(R.id.btnEditCancel)
//            var txtName : EditText = dialog.findViewById(R.id.txtEditStudentName)
//            var txtAge : EditText = dialog.findViewById(R.id.txtEditStudentAge)
//
//            txtAge.setText(student.age.toString())
//            txtName.setText(student.name)
//
//            btnUpdate.setOnClickListener(View.OnClickListener {
//                student.name = txtName.text.toString()
//                student.age = txtAge.text.toString().toInt()
//                MainActivity.database?.QueryData("UPDATE Student set Name = '${student.name}', Age = ${student.age} WHERE Id = ${student.id}")
//                studentList[position] = student
//                notifyDataSetChanged()
//                dialog.dismiss()
//            })
//
//            btnCancel.setOnClickListener(View.OnClickListener { dialog.dismiss() })
//
//            dialog.show()
//        })
//
//        holder.btnDelete.setOnClickListener(View.OnClickListener {
//            MainActivity.database?.QueryData("DELETE FROM Student WHERE Id = ${student.id}")
//            studentList.removeAt(position)
//            notifyDataSetChanged()
//        })


        return view

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(position: Int): Any {
        return itemList.get(position)
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemId(position: Int): Long {
        return 0
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        return itemList.size
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}