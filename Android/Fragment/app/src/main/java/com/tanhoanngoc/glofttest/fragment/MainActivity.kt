package com.tanhoanngoc.glofttest.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fragmentManager : FragmentManager = supportFragmentManager
        var fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
        var fragmentA : FragmentA = FragmentA()
        var fragmentB: FragmentB = FragmentB()

        fragmentTransaction.add(R.id.frameLayout1, fragmentA)
        fragmentTransaction.add(R.id.frameLayout1, fragmentB)

        fragmentTransaction.commit()

    }
}
