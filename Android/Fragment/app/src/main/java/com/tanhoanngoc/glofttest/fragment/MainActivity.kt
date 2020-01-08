package com.tanhoanngoc.glofttest.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initEvent()
    }

    fun onButtonClicked(view: View){
        var fragmentManager : FragmentManager = supportFragmentManager
        var fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()

        when(view.id){
            R.id.btnAddFragA -> { addFragment(FragmentA(), "fragA") }
            R.id.btnAddFragB -> { addFragment(FragmentB(), "fragB") }
            R.id.btnAddFragC -> { addFragment(FragmentC(), "fragC") }

            R.id.btnRemoveFragA -> { removeFragment(supportFragmentManager.findFragmentByTag("fragA") as FragmentA) }
            R.id.btnRemoveFragB -> { removeFragment(supportFragmentManager.findFragmentByTag("fragB") as FragmentB) }
            R.id.btnRemoveFragC -> { removeFragment(supportFragmentManager.findFragmentByTag("fragC") as FragmentC) }
        }
    }

    fun addFragment(fragment : Fragment, tag : String){
        var fragmentManager : FragmentManager = supportFragmentManager
        var fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frameLayout1, fragment, tag)
        fragmentTransaction.addToBackStack(tag)
        fragmentTransaction.commit()
    }

    fun removeFragment(fragment: Fragment){
        var fragmentManager : FragmentManager = supportFragmentManager
        var fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
        if(fragment != null){
            fragmentTransaction.remove(fragment)
            fragmentTransaction.commit()
        }else{
            Toast.makeText(this, "this fragment doesn't exits!", Toast.LENGTH_SHORT).show()
        }
    }

    fun backStack(view : View){
        supportFragmentManager.popBackStack()
    }

    fun popFragmentA(view: View){
        supportFragmentManager.popBackStack("fragA", 0)
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStack()
        }else{
            super.onBackPressed()
        }

    }

    fun initEvent(){
        btnChangeText.setOnClickListener(View.OnClickListener {
            var fragmentA : FragmentA = supportFragmentManager.findFragmentById(R.id.fragmentA) as FragmentA
            //fragmentA.txtHello.setText("Change by Main Activity")
            fragmentA.setHelloText("Change by Main Activity")
        })


        btnAddFragA.setOnClickListener(View.OnClickListener {
            var fragmentManager : FragmentManager = supportFragmentManager
            var fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
            var fragmentA : FragmentA = FragmentA()

            var bundle : Bundle = Bundle()
            bundle.putString("name", "Nguyen Van A")
            fragmentA.arguments = bundle

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
