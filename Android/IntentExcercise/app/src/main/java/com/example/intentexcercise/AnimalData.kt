package com.example.intentexcercise

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

class Animal{
    var name : String = ""
    var pictureID : Int = -1

    constructor(name: String, pictureID: Int) {
        this.name = name
        this.pictureID = pictureID
    }
}

class AnimalAdapter(var layoutID : Int, var animalData : ArrayList<Animal>,  var context : Context) :
    BaseAdapter() {

    class ViewHolder(var view : View){
        var imgAnimal : ImageView = view.findViewById(R.id.img_AnimalPic)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder : ViewHolder
        var view : View?

        if(convertView == null){
            var inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(layoutID, null) as View
            holder = ViewHolder(view)
            view.setTag(holder)
        }else{
            view = convertView
            holder = view.tag as ViewHolder
        }

        var animal = animalData.get(position)
        holder.imgAnimal.setImageResource(animal.pictureID)

        return view as View
    }

    override fun getItem(position: Int): Any {
        return animalData.get(position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return animalData.size
    }

}