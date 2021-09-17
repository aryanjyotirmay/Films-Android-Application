package com.aryanjyotirmay.films

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageButton

class MainActivity2 : AppCompatActivity() {
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val home = findViewById<ImageButton>(R.id.home_button)
        home.setOnClickListener {
            super.onBackPressed()
        }

        webView = findViewById(R.id.web_view_yt)

        val searchMovieTerm = intent?.extras?.getString("movie_url").toString()

        Navigator.loadWebViewNews(searchMovieTerm,webView)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}