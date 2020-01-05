package com.example.RSS

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_magazine_detail.*

class MagazineDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_magazine_detail)

        var link : String = intent.getStringExtra("link")
        wv_magazine.loadUrl(link)
    }
}
