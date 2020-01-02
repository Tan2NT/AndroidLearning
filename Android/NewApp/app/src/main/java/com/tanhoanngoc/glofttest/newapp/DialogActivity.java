package com.tanhoanngoc.glofttest.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class DialogActivity extends Activity {

    RelativeLayout rlRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitLayout();
        //setContentView(R.layout.activity_dialog);
        setContentView(rlRoot);
    }

    private void InitLayout(){
        rlRoot = new RelativeLayout(this);
        rlRoot.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rlRoot.setBackground(getResources().getDrawable(R.drawable.thangcoder));

        LinearLayout lnSub = new LinearLayout(this);
        LinearLayout.LayoutParams lay =  (new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        lay.gravity = Gravity.CENTER;
        lnSub.setOrientation(LinearLayout.VERTICAL);
        lnSub.setLayoutParams(lay);

        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.facebook_icon);

        lnSub.addView(img);

        rlRoot.addView(lnSub);


    }
}
