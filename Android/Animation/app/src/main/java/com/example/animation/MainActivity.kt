package com.example.animation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // alpha anim
        var alphaAnim : Animation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha)
        img_alpha.setOnClickListener(View.OnClickListener {
            img_alpha.startAnimation(alphaAnim)
        })

        // rotate anim
        var rotateAnim : Animation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate)
        img_rotate.setOnClickListener(View.OnClickListener {
            img_rotate.startAnimation(rotateAnim)
        })

        // scale anim
        var scaleAnim : Animation = AnimationUtils.loadAnimation(this, R.anim.anim_scale)
        img_scale.setOnClickListener(View.OnClickListener {
            img_scale.startAnimation(scaleAnim)
        })

        // scale anim
        var tranAnim : Animation = AnimationUtils.loadAnimation(this, R.anim.anim_translate)
        img_translate.setOnClickListener(View.OnClickListener {
            img_translate.startAnimation(tranAnim)
        })

        btn_Login.setOnClickListener(View.OnClickListener {
            var loginIntent : Intent = Intent()
            loginIntent.setClass(this, LoginActivity::class.java)
            startActivity(loginIntent)
            overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit)
        })
    }


}
