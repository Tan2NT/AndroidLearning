package com.tanhoanngoc.glofttest.newapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    private ListView lvFruit;
    ArrayList<Fruit> arrFruit;
    FruitAdapter     adapterFruit;

    private Button btn_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fruit_listview_layout);
        Log.i(TAG, "---------- onCreate -------");

//        btn_Login = findViewById(R.id.btn_switchLogin);
//        btn_Login.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public  void onClick(View v){
//                Intent it = new Intent(MainActivity.this, DialogActivity.class);
//                startActivity(it);
//            }
//        });

        handleFruitLayout();
    }

    private void handleFruitLayout(){
        lvFruit = findViewById(R.id.lv_fruits);
        arrFruit = new ArrayList<Fruit>();
        arrFruit.add(new Fruit("Banana", "banana is a ..... special for a girl ...", R.drawable.banana));
        arrFruit.add(new Fruit("apple", "Apple is a ..... special for Enxtain ...", R.drawable.apple));
        arrFruit.add(new Fruit("Cherries", "cherries is a ..... special for a girl lip ...", R.drawable.cherries));
        arrFruit.add(new Fruit("Coconutmeat", "coconutmeat is a ..... special in the South of Vietnam ...", R.drawable.coconutmeat));
        arrFruit.add(new Fruit("Grape", "Grape is a ..... i don't know anymore about it ...", R.drawable.grapefruit));
        arrFruit.add(new Fruit("Grape", "Grape is a ..... i don't know anymore about it ...", R.drawable.grapefruit));
        arrFruit.add(new Fruit("Grape", "Grape is a ..... i don't know anymore about it ...", R.drawable.grapefruit));
        arrFruit.add(new Fruit("Grape", "Grape is a ..... i don't know anymore about it ...", R.drawable.grapefruit));
        arrFruit.add(new Fruit("Grape", "Grape is a ..... i don't know anymore about it ...", R.drawable.grapefruit));
        arrFruit.add(new Fruit("Grape", "Grape is a ..... i don't know anymore about it ...", R.drawable.grapefruit));

        adapterFruit = new FruitAdapter(MainActivity.this, R.layout.fruit_listview_item, arrFruit);

        lvFruit.setAdapter(adapterFruit);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(TAG, "---------- onStart -------");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(TAG, "---------- onResume -------");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(TAG, "---------- onPause -------");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.i(TAG, "---------- onRestart -------");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(TAG, "---------- onDestroy -------");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(TAG, "---------- onStop -------");
    }


}
