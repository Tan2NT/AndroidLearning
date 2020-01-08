package com.tanhoanngoc.glofttest.listfragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class StudentAdapter (var context: Context, var layoutId : Int, var studentList : ArrayList<Student>) : BaseAdapter(){

    class ViewHolder(var view : View){
        var txtName : TextView = view.findViewById(R.id.txt_rowName)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder : ViewHolder
        var view : View
        if(convertView == null){
            var inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.row_student_detail, null)
            holder = ViewHolder(view)
            view.setTag(holder)
        }else{
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        holder.txtName.setText(studentList.get(position).name)

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