package com.tanhoanngoc.glofttest.listfragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        btnOpenDialog.setOnClickListener(View.OnClickListener {
//            var dialog : MyFragmentDialog = MyFragmentDialog()
//            dialog.show(supportFragmentManager, "dialog")
//        })
    }
}
