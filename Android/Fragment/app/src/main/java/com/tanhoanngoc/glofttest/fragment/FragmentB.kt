package com.tanhoanngoc.glofttest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

class FragmentB : Fragment(){

    lateinit var txtTextB : EditText
    lateinit var btnB : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view : View = inflater.inflate(R.layout.fragment_b, container, false)
        txtTextB = view.findViewById(R.id.txtFragmentB)
        btnB = view.findViewById(R.id.btnButtonB)

        btnB.setOnClickListener(View.OnClickListener {
            var txtA : EditText = activity?.findViewById(R.id.txtFragAName) as EditText
            txtA.setText(txtTextB.text.toString())
        })

        return view
    }
}