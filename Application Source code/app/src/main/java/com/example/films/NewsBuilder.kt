package com.example.films

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NewsBuilder {
    private val client = OkHttpClient.Builder().build()

    private val newsRetrofit = Retrofit.Builder()
        .baseUrl("https://newsapi.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(NewsBuilder.client)
        .build()

    fun <T> buildNewsService(service: Class<T>): T {
        return NewsBuilder.newsRetrofit.create(service)
    }
}