package com.tanhoanngoc.glofttest.myapplication

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_custom.*
import kotlinx.android.synthetic.main.fruit_listview_layout.*
import kotlinx.android.synthetic.main.grid_view_basic.*
import kotlinx.android.synthetic.main.grid_view_custom_hotgirl.*

import kotlinx.android.synthetic.main.list_view_simple.*
import java.text.SimpleDateFormat


class MainActivity : AppCompatActivity() {

    private val TAG : String = "TDebug";

    private var subjectData : ArrayList<String> = ArrayList()

    //Gridview
    private var friendList : List<String> = listOf ("Tri", "Thai", "Trinh", "Thanh", "Thang", "Mui")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fruit_listview_layout)

        //b5_When_Statement()

        //b6_While_Statement()

        //b7_For_Statement()

        //b8_Array()

        //TestOOP()

        //listViewSimple()

        listViewCustome()

       // handleComnonButton()

        //handleGridviewBasic()

        //handleCustomGridview();

//        handlePopupMenu()
//
//        handleForContextMenu()
//
//        handleLoginDialog()
//
//        handleDatepicker()
    }

    private fun handleDatepicker(){
        if(Build.VERSION.SDK_INT >= 24){
            edtDatePicker.setOnClickListener(View.OnClickListener {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    // Display Selected date in textbox
                    var cal : Calendar = Calendar.getInstance()
                    cal.set(year, monthOfYear, dayOfMonth)
                    var dateFormat : SimpleDateFormat = SimpleDateFormat("dd/mm/yyyy")
                    edtDatePicker.setText(dateFormat.format(cal.time))
                    Toast.makeText(this, "${dayOfMonth} / ${monthOfYear} / ${year}", Toast.LENGTH_SHORT).show()
                }, year, month, day)

                dpd.show()

            })
        }

    }

    private fun handleLoginDialog(){
        btnCustomDialog.setOnClickListener(View.OnClickListener {
            showDialog()
        })
    }

    private fun showDialog(){
        var dialog : Dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_custom)
        dialog.setCanceledOnTouchOutside(false)

        dialog.btnCancel.setOnClickListener(View.OnClickListener { dialog.dismiss() })

        dialog.show()
    }

    private fun handleForContextMenu(){
        registerForContextMenu(btnContextMenu)
    }

    private fun handlePopupMenu(){
        btnPopupMenu.setOnClickListener(View.OnClickListener {
            showPopupMenu()
        })
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu, menu)
        menu?.setHeaderTitle("Chose color")
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuYellow -> mainLayout.setBackgroundColor(Color.YELLOW)
            R.id.menuRed -> mainLayout.setBackgroundColor(Color.RED)
            R.id.menuGreen -> mainLayout.setBackgroundColor(Color.GREEN)
        }

        return super.onContextItemSelected(item)
    }

    private fun showPopupMenu(){
        var popup : PopupMenu = PopupMenu(this, btnPopupMenu)
        popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)

        popup.setOnMenuItemClickListener { item: MenuItem ->
            when(item.itemId){
                R.id.menuEdit -> Toast.makeText(this, "You chose Edit", Toast.LENGTH_SHORT).show()
                R.id.menuDelete -> Toast.makeText(this, "You chose Delete", Toast.LENGTH_SHORT).show()
                R.id.menuInsert -> Toast.makeText(this, "You chose Insert", Toast.LENGTH_SHORT).show()
            }

            true
        }

        popup.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.optionSettings -> {
                Toast.makeText(this, "You chose Settigns", Toast.LENGTH_SHORT).show()

            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun  handleCustomGridview(){
        var hotGirlData : ArrayList<HotGirl> = ArrayList()
        hotGirlData.add(HotGirl("girl_1", R.drawable.girl_1))
        hotGirlData.add(HotGirl("girl_2", R.drawable.girl_2))
        hotGirlData.add(HotGirl("girl_3", R.drawable.girl_3))
        hotGirlData.add(HotGirl("girl_4", R.drawable.girl_4))
        hotGirlData.add(HotGirl("girl_5", R.drawable.girl_5))
        hotGirlData.add(HotGirl("girl_6", R.drawable.girl_6))
        hotGirlData.add(HotGirl("girl_7", R.drawable.girl_7))
        hotGirlData.add(HotGirl("girl_8", R.drawable.girl_8))
        hotGirlData.add(HotGirl("girl_9", R.drawable.girl_9))

        gvHotGirls.numColumns = 3;
        gvHotGirls.adapter = HotGirlAdapter(this, R.layout.grid_view_item_hot_girl_detail, hotGirlData)
    }

    private fun  handleGridviewBasic(){
        // Fill data
        gvFriends.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, friendList)
        gvFriends.numColumns = 3

        // handle click action
        gvFriends.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, "Selected friend is ${friendList.get(position)}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleComnonButton(){
        cbRobot.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
                Toast.makeText(this, "You are not a Robot, congratulation !", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this, "You are a Robot so you are not allowed to login !", Toast.LENGTH_SHORT).show()
        }

        radioGroupGender.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radioFemale -> radioFemale.setBackgroundColor(Color.RED)
                R.id.radioMale -> radioMale.setBackgroundColor(Color.GREEN)
            }
        }

        // handle seek bar & progress bar
        seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    var seekBarMax : Int = seekBar?.max ?: 0;
                    progressBar.setProgress(progress * progressBar.max/seekBarMax )
                    return
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    return
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    return
                }

            }
        )

        var cdt = object : CountDownTimer(10000, 1000){
            override fun onFinish() {
                Toast.makeText(applicationContext, "Download succeed !", Toast.LENGTH_SHORT).show()
                return
            }

            override fun onTick(millisUntilFinished: Long) {
                seekBar.setProgress(seekBar.progress + 1)
            }

        }.start()
    }

    private fun listViewCustome(){
        var fruitData : ArrayList<Fruit> = ArrayList()
        fruitData.add(Fruit("Banana", "banana is a ..... special for a girl ...", R.drawable.banana))
        fruitData.add(Fruit("Cherries", "cherries is a ..... special for a girl lip ...", R.drawable.cherries));
        fruitData.add(Fruit("Coconutmeat", "coconutmeat is a ..... special in the South of Vietnam ...", R.drawable.coconutmeat));
        fruitData.add(Fruit("Grape", "Grape is a ..... i don't know anymore about it ...", R.drawable.coconutmeat));
        fruitData.add(Fruit("Banana", "banana is a ..... special for a girl ...", R.drawable.banana))
        fruitData.add(Fruit("Cherries", "cherries is a ..... special for a girl lip ...", R.drawable.cherries));
        fruitData.add(Fruit("Coconutmeat", "coconutmeat is a ..... special in the South of Vietnam ...", R.drawable.coconutmeat));
        fruitData.add(Fruit("Grape", "Grape is a ..... i don't know anymore about it ...", R.drawable.coconutmeat));
        fruitData.add(Fruit("Banana", "banana is a ..... special for a girl ...", R.drawable.banana))
        fruitData.add(Fruit("Cherries", "cherries is a ..... special for a girl lip ...", R.drawable.cherries));
        fruitData.add(Fruit("Coconutmeat", "coconutmeat is a ..... special in the South of Vietnam ...", R.drawable.coconutmeat));
        fruitData.add(Fruit("Grape", "Grape is a ..... i don't know anymore about it ...", R.drawable.coconutmeat));
        fruitData.add(Fruit("Banana", "banana is a ..... special for a girl ...", R.drawable.banana))
        fruitData.add(Fruit("Cherries", "cherries is a ..... special for a girl lip ...", R.drawable.cherries));
        fruitData.add(Fruit("Coconutmeat", "coconutmeat is a ..... special in the South of Vietnam ...", R.drawable.coconutmeat));
        fruitData.add(Fruit("Grape", "Grape is a ..... i don't know anymore about it ...", R.drawable.coconutmeat));
        var  fruitAdapter : FruitAdapter = FruitAdapter(this, fruitData, R.layout.fruit_listview_item);
        lv_fruits.adapter = fruitAdapter

        lv_fruits.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, "You clicked in ${fruitData.get(position).getName()}", Toast.LENGTH_SHORT).show()
        }
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
