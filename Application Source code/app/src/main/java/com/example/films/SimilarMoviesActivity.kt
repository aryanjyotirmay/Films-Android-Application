package com.example.films

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
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

class SimilarMoviesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_similar_movies)
        val head:TextView=findViewById(R.id.headerText)
        val id = intent?.extras?.getString("id")!!.toInt()
        val title = intent?.extras?.getString("title")
 //       val background = intent?.extras?.getString("themeImage")
        val req = ServiceBuilder.buildService(TmdbEndpoints::class.java)
        val callSimilar = req.getSimilar(id,Constant.apiKey)
        val imgBack:ImageView=findViewById(R.id.backImage)
        head.text = "Movies Similar to: ${title}"

        callSimilar.enqueue(object : Callback<PopularMovies> {
            override fun onResponse(call: Call<PopularMovies>, response: Response<PopularMovies>) {
                if (response.isSuccessful) {

                    recyclerViewSimilar.apply {
                        setHasFixedSize(true)
                        layoutManager = GridLayoutManager(this@SimilarMoviesActivity,2)
                        adapter = MoviesAdapter(response.body()!!.results)
                    }

                }
                if(response.body()!!.results.count()==0){
                    val drawable: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.screenshot_2021_06_18_at_22_31_12, null)
                    Glide.with(this@SimilarMoviesActivity).load(drawable).into(imgBack)

                }
            }

            override fun onFailure(call: Call<PopularMovies>, t: Throwable) {
                Toast.makeText(this@SimilarMoviesActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })



    }
}