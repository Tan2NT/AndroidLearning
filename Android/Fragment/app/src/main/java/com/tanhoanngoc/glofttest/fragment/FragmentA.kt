package com.tanhoanngoc.glofttest.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_a.*

public class FragmentA : Fragment(){

    lateinit var btnHello : Button
    lateinit var txtHello : EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view : View  = inflater.inflate(R.layout.fragment_a, container, false)
        btnHello = view.findViewById(R.id.btnFragASayHello)
        txtHello = view.findViewById(R.id.txtFragAName)

        btnHello.setOnClickListener(View.OnClickListener {
            activity?.txtSayHello?.setText(txtHello.text.toString())
        })

        //return super.onCreateView(inflater, container, savedInstanceState)
        return view
    }

    fun setHelloText(content : String){
        txtHello.setText((content))
    }
}