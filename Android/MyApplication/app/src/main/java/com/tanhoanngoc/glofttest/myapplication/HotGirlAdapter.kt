package com.tanhoanngoc.glofttest.myapplication

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

class HotGirlAdapter(var context : Context, var layoutID : Int, var data : ArrayList<HotGirl>) : BaseAdapter() {

    class ViewHolder(var view : View){
        var pic : ImageView = view.findViewById(R.id.hot_girl_pic)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder : ViewHolder? = null;
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

        var hg: HotGirl = data.get(position)
        holder.pic.setImageResource(hg.picID)

        return view as View

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(position: Int): Any {
        return 0
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemId(position: Int): Long {
        return 0;
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        return data.size
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}