package com.tanhoanngoc.glofttest.myapplication

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.*

class FruitAdapter(var context: Context, var data : ArrayList<Fruit>, var layoutID: Int) : BaseAdapter() {

    class FruitHolder(var view: View){

        var txtName : TextView = view.findViewById(R.id.lv_item_fruit_name)
        var txtDes : TextView = view.findViewById(R.id.lv_item_fruit_desciption)
        var image : ImageView = view.findViewById(R.id.lv_item_fruit_image)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view : View?
        var holder : FruitHolder

        if(convertView == null){
            var inflater : LayoutInflater = LayoutInflater.from(context);
            view = inflater.inflate(layoutID, null)

            holder = FruitHolder(view)
            view.tag = holder
        }else{
            view = convertView;
            holder = convertView.tag as FruitHolder
        }

        var fruit : Fruit = data.get(position)
        holder.txtName.setText(fruit.getName())
        holder.txtDes.setText(fruit.getDescription())
        holder.image.setImageResource(fruit.getPictureId())

        var scaleAnim : Animation = AnimationUtils.loadAnimation(context, R.anim.anim_scale)
        view?.startAnimation(scaleAnim)

        return view as View;
    }

    override fun getItem(position: Int): Any {
        return 0
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemId(position: Int): Long {
        return 0
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        return data.size
    }
}