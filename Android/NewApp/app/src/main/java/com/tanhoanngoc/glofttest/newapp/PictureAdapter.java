package com.tanhoanngoc.glofttest.newapp;

import android.content.Context;
import android.graphics.Picture;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class PictureAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Picturel> arrPic;
    private int layout;


    public PictureAdapter(Context context, ArrayList<Picturel> arrPic, int layout) {
        this.context = context;
        this.arrPic = arrPic;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return arrPic.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class PictureHolder{
        public ImageView imgPic;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PictureHolder holder = new PictureHolder();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            holder.imgPic = convertView.findViewById(R.id.img_item_detail);
            convertView.setTag(holder);
        }else{
            holder = (PictureHolder) convertView.getTag();
        }

        Picturel pic = arrPic.get(position);

        holder.imgPic.setImageResource(pic.getPicID());

        return convertView;
    }
}
