package com.example.films

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.films.databinding.ActivityDetailsBinding
import kotlinx.android.synthetic.main.activity_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsActivity : AppCompatActivity() {
    lateinit var viewModel: MoviesViewModel
    private var themeImage: String? = null
    private lateinit var id: String
    private var posPath: String? = null
    private var name: String? = null
    private lateinit var dBinding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dBinding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(dBinding.root)

        val buttonMain: ImageButton = findViewById(R.id.button2)
        buttonMain.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }
        id = intent?.extras?.getInt("id").toString()
        val bar: ProgressBar = findViewById(R.id.progressBar)
        val titleMov: TextView = findViewById(R.id.title_2)
        val overviewMov: TextView = findViewById(R.id.overview_2)
        overviewMov.movementMethod = ScrollingMovementMethod()
        val relDate: TextView = findViewById(R.id.textView4)
        val imgdisp: ImageView = findViewById(R.id.poster_2)
        val imgbg: ImageView = findViewById(R.id.imageView)
        val stars: TextView = findViewById(R.id.rating_2)

        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(MoviesViewModel::class.java)

        dBinding.buttonShare.setOnClickListener {
            shareButton()
        }

        dBinding.button3.setOnClickListener {
            seeReviews()
        }

        dBinding.poster2.setOnClickListener {
            zoomImg()
        }

        dBinding.button5.setOnClickListener {
            similarMovies()
        }



        bar.visibility = View.VISIBLE
        val req = ServiceBuilder.buildService(TmdbEndpoints::class.java)
        val called = req.getDetails(id.toInt(), Constant.apiKey)
        val castCall = req.getCast(id.toInt(), Constant.apiKey)
        called.enqueue(object : Callback<MovieDetails> {

            override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
                Log.d(TAG, "onResponse: Working till this point")
                if (response.isSuccessful) {
                    val movieDet = response.body()!!
                    Log.d(TAG, "onResponse: received response")
                    if (movieDet.overview.count() == 0) {
                        overviewMov.text = "No Overview"
                    } else {
                        overviewMov.text = movieDet.overview
                    }
                    relDate.text = "Date:" + movieDet.release_date
                    titleMov.text = movieDet.title
                    name = movieDet.title
                    stars.text = "★: ${movieDet.vote_average}"
                    themeImage = response.body()?.backdrop_path
                    posPath = movieDet.poster_path.toString()
                    Glide.with(this@DetailsActivity)
                        .load("https://image.tmdb.org/t/p/w500${posPath}").into(imgdisp)
                    Glide.with(this@DetailsActivity)
                        .load("https://image.tmdb.org/t/p/w500${response.body()?.backdrop_path}")
                        .listener(object :
                            RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                bar.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                bar.visibility = View.GONE
                                return false
                            }
                        }).into(imgbg)


                    dBinding.btnFav.setOnClickListener {

                        val mov = movieDet.id.toString()
                        if (mov.isNotEmpty()){

                            viewModel.insertMovie(FavouritesTable(mov))
                        }


                    }

                }

            }

            override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                Toast.makeText(this@DetailsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }


        })

        castCall.enqueue(object : Callback<CastData> {
            override fun onResponse(call: Call<CastData>, response: Response<CastData>) {
                Log.d(TAG, "onResponse:before if loop ")
                Log.d(TAG, "onResponse: Cast called")
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: inside if loop")
                    cast_recycler.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(
                            this@DetailsActivity,
                            LinearLayoutManager.HORIZONTAL, false
                        )
                        adapter = CastAdapter(response.body()!!.cast)
                    }

                }
            }

            override fun onFailure(call: Call<CastData>, t: Throwable) {
                Toast.makeText(this@DetailsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })


    }


    fun shareButton() {

        val sh = Intent(Intent.ACTION_SEND)
        sh.type = "text/plain"
        sh.putExtra(
            Intent.EXTRA_TEXT,
            "Come watch '$name' with me: https://image.tmdb.org/t/p/w500$posPath"
        )

        val choose = Intent.createChooser(sh, "Share this movie")
        startActivity(choose)

    }


    fun zoomImg() {
        val i: Intent = Intent(this, enlarge::class.java)
        i.putExtra("imgurl", posPath)
        startActivity(i)

    }

    fun similarMovies() {

        val intent = Intent(this, SimilarMoviesActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("title", name)
        intent.putExtra("themeImage", themeImage)
        startActivity(intent)


    }

    fun seeReviews() {

        val reviewIntent = Intent(this, ImageCollections::class.java)
        reviewIntent.putExtra("id", id)
        reviewIntent.putExtra("title", name)
        startActivity(reviewIntent)
    }

}
