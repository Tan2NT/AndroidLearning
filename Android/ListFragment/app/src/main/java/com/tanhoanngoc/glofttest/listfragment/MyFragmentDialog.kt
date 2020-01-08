package com.tanhoanngoc.glofttest.listfragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.method.DialerKeyListener
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class MyFragmentDialog : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var builer = AlertDialog.Builder(activity)

        builer.setTitle("Confirmation")
        builer.setMessage("Are you want to delete this product?")

       builer.setPositiveButton("YES", object :DialogInterface.OnClickListener {
           override fun onClick(dialog: DialogInterface?, which: Int) {
               Toast.makeText(activity, "OK ----", Toast.LENGTH_SHORT).show()
           }
       })

        builer.setNegativeButton("NO", object :DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                Toast.makeText(activity, "NO ----", Toast.LENGTH_SHORT).show()
            }
        })

        return builer.create()
    }
}