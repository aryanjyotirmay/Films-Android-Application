package com.example.films

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton

class WebViewActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val newsArticle = intent?.extras?.getString("urlToArticle").toString()
        val home = findViewById<ImageButton>(R.id.home_button_news)
        val share = findViewById<ImageButton>(R.id.button_share)
        home.setOnClickListener {
            super.onBackPressed()
        }
        share.setOnClickListener {
            Navigator.shareButtonArticle(this@WebViewActivity,newsArticle)
        }

        webView = findViewById(R.id.web_view_news)

        Navigator.loadWebViewNews(newsArticle,webView)

    }


    override fun onBackPressed() {

        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

}