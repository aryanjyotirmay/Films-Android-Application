package com.aryanjyotirmay.films

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

object Navigator {
    fun zoomImg(context: Context, posPath: String) {
        if (posPath != null) {
            val i: Intent = Intent(context, enlarge::class.java)
            i.putExtra("imgurl", posPath)
            context.startActivity(i)
        } else {
            Toast.makeText(context, "No Image :(", Toast.LENGTH_SHORT).show()
        }
    }

    fun similarMovies(context: Context, id: String, name: String, themeImage: String) {
        val intent = Intent(context, SimilarMoviesActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("title", name)
        intent.putExtra("themeImage", themeImage)
        context.startActivity(intent)
    }

    fun seeReviews(context: Context, id: String, name: String) {
        val reviewIntent = Intent(context, ImageCollections::class.java)
        reviewIntent.putExtra("id", id)
        reviewIntent.putExtra("title", name)
        context.startActivity(reviewIntent)
    }

    fun searchHandler(context: Context, searchTerm: String) {
        if (searchTerm.count() != 0) {
            val i = Intent(context, SearchActivity::class.java)
            i.putExtra("search", searchTerm)
            context.startActivity(i)
        }
    }

    fun shareButtonA(context: Context, posActor: String, nameShare: String) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(
            Intent.EXTRA_TEXT,
            "Hey! Check ${nameShare} out: https://image.tmdb.org/t/p/w500${posActor}"
        )
        val chooser = Intent.createChooser(share, "Share Actor")
        if (posActor != null) {
            context.startActivity(chooser)
        }
        if (posActor == null) {
            Toast.makeText(context, "No data to share", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareButton(context: Context, name: String, posPath: String) {
        val sh = Intent(Intent.ACTION_SEND)
        sh.type = "text/plain"
        sh.putExtra(
            Intent.EXTRA_TEXT,
            "Come watch '$name' with me: https://image.tmdb.org/t/p/w500$posPath"
        )
        val choose = Intent.createChooser(sh, "Share this movie")
        context.startActivity(choose)
    }

    fun shareButtonArticle(context: Context, url: String) {
        val ar = Intent(Intent.ACTION_SEND)
        ar.type = "text/plain"
        ar.putExtra(
            Intent.EXTRA_TEXT,
            url
        )
        val articleChooser = Intent.createChooser(ar, "Share this article")
        context.startActivity(articleChooser)
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun loadWebViewNews(url: String, webView: WebView) {
        webView.webViewClient = WebViewClient()
        webView.apply {
            loadUrl(url)
            settings.javaScriptEnabled = true
        }
    }
}