package com.aryanjyotirmay.films

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.large.*

class enlarge:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.large)


        val nIntent:Intent = intent

        val str = nIntent.getStringExtra("imgurl")
        Glide.with(this).load("https://image.tmdb.org/t/p/w500${str}").into(zoomed)
    }


}