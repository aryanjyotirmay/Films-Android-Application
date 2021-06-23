package com.example.films

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_similar_movies.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_similar_movies)


        val searchTerm = intent?.extras?.getString("search").toString()

        val req = ServiceBuilder.buildService(TmdbEndpoints::class.java)
        val callSearch = req.searchMovie(Constant.apiKey,searchTerm)
        val header:TextView = findViewById(R.id.headerText)
        header.text = "Search results for: ${searchTerm}"

        callSearch.enqueue(object : Callback<PopularMovies> {
            override fun onResponse(call: Call<PopularMovies>, response: Response<PopularMovies>) {
                if (response.isSuccessful) {

                    recyclerViewSimilar.apply {
                        setHasFixedSize(true)
                        layoutManager = GridLayoutManager(this@SearchActivity,2)
                        adapter = MoviesAdapter(response.body()!!.results)
                    }

                }
                if (response.body()!!.results.count()==0){
                    val drawable: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.screenshot_2021_06_18_at_22_31_12, null)
                    Glide.with(this@SearchActivity).load(drawable).into(backImage)
                }
            }

            override fun onFailure(call: Call<PopularMovies>, t: Throwable) {
                Toast.makeText(this@SearchActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}