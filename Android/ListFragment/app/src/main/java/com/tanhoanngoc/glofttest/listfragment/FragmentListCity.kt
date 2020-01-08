package com.tanhoanngoc.glofttest.listfragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.ListFragment

class FragmentListCity : ListFragment(){

    var arrayCity : Array<String> = arrayOf("Ha Noi", "Hue", "Da Nang", "Nha Trang", "Ho Chi Minh");

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       listAdapter = ArrayAdapter(activity?.applicationContext as Context, android.R.layout.simple_list_item_1, arrayCity)

        return inflater.inflate(R.layout.fragment_list_city, container, false)
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        Toast.makeText(activity?.applicationContext, arrayCity.get(position), Toast.LENGTH_SHORT).show()
        super.onListItemClick(l, v, position, id)
    }

}