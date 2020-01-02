package com.tanhoanngoc.glofttest.newapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FruitAdapter extends BaseAdapter {

    // Members
    private Context context;
    private int layout;                 // layout to display each item in list view
    private ArrayList<Fruit> fruitList;

    public FruitAdapter(Context context, int layout, ArrayList<Fruit> fruitList) {
        this.context = context;
        this.layout = layout;
        this.fruitList = fruitList;
    }

    @Override
    public int getCount() {
        return fruitList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        public ImageView imgPic;
        public TextView txtName, txtDes;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            holder = new ViewHolder();

            holder.imgPic = convertView.findViewById(R.id.lv_item_fruit_image);
            holder.txtName = convertView.findViewById(R.id.lv_item_fruit_name);
            holder.txtDes = convertView.findViewById(R.id.lv_item_fruit_desciption);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Fruit fruit = fruitList.get(position);

        holder.imgPic.setImageResource(fruit.getIgmID());
        holder.txtName.setText(fruit.getName());
        holder.txtDes.setText(fruit.getDescription());

        return convertView;
    }
}
