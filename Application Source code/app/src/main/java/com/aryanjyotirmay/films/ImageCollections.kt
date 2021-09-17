package com.aryanjyotirmay.films

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_similar_movies.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageCollections : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_similar_movies)

        val id = intent?.extras?.getString("id").toString()
        val movieName = intent?.extras?.getString("title")
        val header: TextView = findViewById(R.id.headerText)

        header.text = "Reviews for: ${movieName}"

        val reviewRequest = ServiceBuilder.buildService(TmdbEndpoints::class.java)
        val reviewCall = reviewRequest.getReviews(id.toInt(), Constant.apiKey)


        reviewCall.enqueue(object : Callback<ReviewData> {
            override fun onResponse(call: Call<ReviewData>, response: Response<ReviewData>) {

                if (response.isSuccessful) {

                    recyclerViewSimilar.apply {

                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@ImageCollections)
                        adapter = ReviewAdapter(response.body()!!.results)

                    }

                }

                if (response.body()!!.results.count() == 0) {

                    val draw: Drawable? = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.screenshot_2021_06_18_at_22_31_12,
                        null
                    )
                    Glide.with(this@ImageCollections).load(draw).into(backImage)

                }

            }

            override fun onFailure(call: Call<ReviewData>, t: Throwable) {

                Toast.makeText(this@ImageCollections, "${t.message}", Toast.LENGTH_SHORT).show()

            }

        })
    }
}