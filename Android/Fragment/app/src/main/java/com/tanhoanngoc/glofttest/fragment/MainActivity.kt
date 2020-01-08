package com.tanhoanngoc.glofttest.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnAddFragA.setOnClickListener(View.OnClickListener {
            var fragmentManager : FragmentManager = supportFragmentManager
            var fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
            var fragmentA : FragmentA = FragmentA()
            fragmentTransaction.add(R.id.frameLayout1, fragmentA)
            fragmentTransaction.commit()
        })

        btnAddFragB.setOnClickListener(View.OnClickListener {
            var fragmentManager : FragmentManager = supportFragmentManager
            var fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
            var fragmentB: FragmentB = FragmentB()
            //fragmentTransaction.add(R.id.frameLayout1, fragmentB)
            fragmentTransaction.replace(R.id.frameLayout1, fragmentB)
            fragmentTransaction.commit()
        })
    }

}
