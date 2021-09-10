package com.example.films

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton

class MainActivity2 : AppCompatActivity() {
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        webView = findViewById(R.id.web_view_yt)
        val home = findViewById<ImageButton>(R.id.home_button)
        val searchMovieTerm = intent.extras?.getString("movie_name").toString()

        home.setOnClickListener {
            val intentHome = Intent(this,MainActivity::class.java)
            startActivity(intentHome)
        }

        loadWebView(searchMovieTerm)

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebView(movieToSearch : String) {
        webView.webViewClient = WebViewClient()

        webView.apply {
            loadUrl("https://www.youtube.com/results?search_query=$movieToSearch Trailer")
            settings.javaScriptEnabled = true
        }

    }

    override fun onBackPressed() {

        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

}