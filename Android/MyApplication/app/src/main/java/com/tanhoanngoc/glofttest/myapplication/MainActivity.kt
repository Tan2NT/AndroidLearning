package com.tanhoanngoc.glofttest.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast

import kotlinx.android.synthetic.main.list_view_simple.*


class MainActivity : AppCompatActivity() {

    private val TAG : String = "TDebug";

    private var subjectData : ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_view_simple)

        //b5_When_Statement()

        //b6_While_Statement()

        //b7_For_Statement()

        //b8_Array()

        //TestOOP()

        listViewSimple()
    }

    private fun listViewSimple(){
        subjectData.add("PHP")
        subjectData.add(".Net")
        subjectData.add("Android")
        subjectData.add("Java")

        lvSubject.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, subjectData);
    }

    private fun TestOOP(){
        var st: Student = Student("Tan", "Hue", 1991);
        st.ShowInfo()

        // DAta
        var vl : Vehicle = Vehicle("Motorbike", "Black", 2);
        Log.i(TAG, "This is a " + vl.name + " with " + vl.color + " has  ${vl.numOfSpin.toString()}  spin")
    }

    private fun b5_When_Statement(){
        var name : String? = "Long";
        when(name){
            "Tan", "Long" -> Toast.makeText(applicationContext, "Hello Tan, you a handsome!", Toast.LENGTH_SHORT).show();
            "Tri" -> Toast.makeText(applicationContext, "Hello Tri, you a too think!", Toast.LENGTH_SHORT).show();
        }

        var num : Int = 4;
        when(num){
            in 1..5 -> Log.i(TAG, "Very bad .......");
            in 6..7 -> Log.i(TAG, "medium ......");
            else -> Log.i(TAG, "good ......");
        }
    }
    private fun b6_While_Statement(){
        var i : Int = 0;
        while(i < 10){
            Log.i(TAG, "Hello Tan, repeate " + i.toString());
            i += 1;
        }
    }

    private fun b7_For_Statement(){
        for(i in 1 .. 10){
            Log.i(TAG, "Hello Tan, repeate " + i.toString());
        }

        for(i in 1 until 3){
            Log.i(TAG, "Hello Tan again, repeate " + i.toString());
        }

        for(i in 3 downTo  1){
            Log.i(TAG, "Hello Tan back, repeate " + i.toString());
        }

        for(i in 15 .. 20 step 2){
            Log.i(TAG, "Hello Tan jump, repeate " + i.toString());
        }
    }

    private fun b8_Array(){
        var arrayNum : IntArray = intArrayOf(1, 2, 3, 4, 5, 6);
        Log.i(TAG, " -------- Value at index 2 is " + arrayNum.get(2).toString());

        var arrayName : List<String> = listOf("Tan", "Tri", "Thai", "Trinh");
        Log.i(TAG, " -------- name of member 2 is " + arrayName.get(2).toString());

        var arraySub : ArrayList<String> = ArrayList()
        arraySub.add("Php")
        arraySub.add(".Net")
        arraySub.add("Android")

        Log.i(TAG, "size of arraySub is " + arraySub.size);
        arraySub.remove("Php")
        Log.i(TAG, "size of arraySub is " + arraySub.size);
    }
}
