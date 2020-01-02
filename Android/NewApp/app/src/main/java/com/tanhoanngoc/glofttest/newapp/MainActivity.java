package com.tanhoanngoc.glofttest.newapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    private ListView lvFruit;
    ArrayList<Fruit> arrFruit;
    FruitAdapter     adapterFruit;

    private Button btn_Login;

    // Gridview
    private GridView gvName;
    private String[] arrayName = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T" };

    private GridView gvImage;
    private ArrayList arrPics;

    // menu
    private Button btnCreatePopupMenu;
    private Button btnContextMenu;
    private ConstraintLayout layoutMonitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_test_layout);
        Log.i(TAG, "---------- onCreate -------");

//        btn_Login = findViewById(R.id.btn_switchLogin);
//        btn_Login.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public  void onClick(View v){
//                Intent it = new Intent(MainActivity.this, DialogActivity.class);
//                startActivity(it);
//            }
//        });

        //handleFruitLayout();

        //handleGridViewSimple();

        //handleGridPictures();

        handlePopupMenu();

        handleContextMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_demo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuSettings:
                break;
            case R.id.menuShare:
                break;
            case R.id.menuSearch:
                break;
            case R.id.menuContact:
                break;
            case R.id.menuExit:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_context, menu);
        menu.setHeaderTitle("Select color");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuYellow:
                layoutMonitor.setBackgroundColor(Color.YELLOW);
                break;
            case R.id.menuRed:
                layoutMonitor.setBackgroundColor(Color.RED);
                break;
            case R.id.menuGreen:
                layoutMonitor.setBackgroundColor(Color.GREEN);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void handleContextMenu(){
        btnContextMenu = findViewById(R.id.btnContextMenu);
        layoutMonitor = findViewById(R.id.layoutMonitor);

        //register view to content_menu
        registerForContextMenu(btnContextMenu);
    }

    private void handlePopupMenu(){
        btnCreatePopupMenu = findViewById(R.id.btnPopupMenu);

        btnCreatePopupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu();
            }
        });
    }

    private void showPopupMenu(){
        PopupMenu popupMenu = new PopupMenu(this, btnCreatePopupMenu);
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuInsert:
                        btnCreatePopupMenu.setText("Insert");
                        break;
                    case R.id.menuDelete:
                        btnCreatePopupMenu.setText("Delete");
                        break;
                    case R.id.menuEdit:
                        btnCreatePopupMenu.setText("Edit");
                        break;

                }

                return false;
            }
        });
        popupMenu.show();
    }

    private void handleGridPictures(){
        gvImage = findViewById(R.id.gv_Image);
        arrPics = new ArrayList<Picturel>();
        arrPics.add(new Picturel("girl 1", R.drawable.girl_1));
        arrPics.add(new Picturel("girl 2", R.drawable.girl_2));
        arrPics.add(new Picturel("girl 3", R.drawable.girl_3));
        arrPics.add(new Picturel("girl 4", R.drawable.girl_4));
        arrPics.add(new Picturel("girl 5", R.drawable.girl_5));
        arrPics.add(new Picturel("girl 6", R.drawable.girl_6));
        arrPics.add(new Picturel("girl 7", R.drawable.girl_7));
        arrPics.add(new Picturel("girl 8", R.drawable.girl_8));
        arrPics.add(new Picturel("girl 9", R.drawable.girl_9));
        arrPics.add(new Picturel("girl 1", R.drawable.girl_1));

        PictureAdapter picAdapter = new PictureAdapter(MainActivity.this, arrPics, R.layout.grid_view_image_item);

        gvImage.setAdapter(picAdapter);
    }

    private void handleGridViewSimple(){
        // anh xa
        gvName = findViewById(R.id.gv_Ten);

        // Create an adapter
        ArrayAdapter adaper = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, arrayName);

        // Fill data
        gvName.setAdapter(adaper);

        gvName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Selected character is " + arrayName[position].toString(), Toast.LENGTH_SHORT).show();
            }
        });

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
