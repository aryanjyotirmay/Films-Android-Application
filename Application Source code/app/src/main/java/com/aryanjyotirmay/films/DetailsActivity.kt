package com.aryanjyotirmay.films

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.aryanjyotirmay.films.databinding.ActivityDetailsBinding
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_favourites.*
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
    private lateinit var isOnelinerEnabled: String
    private val movieFavs = "movieFavourites"

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dBinding = DataBindingUtil.setContentView(this, R.layout.activity_details)

        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences(movieFavs, Context.MODE_PRIVATE)


        id = intent?.extras?.getInt("id").toString()

        dBinding.overview2.movementMethod = ScrollingMovementMethod()
        dBinding.overview2.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN ->
                    v.parent.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_UP ->
                    v.parent.requestDisallowInterceptTouchEvent(false)
            }
            false
        }

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(MoviesViewModel::class.java)

        dBinding.poster2.setOnClickListener {
            posPath?.let { it1 -> Navigator.zoomImg(this, it1) }
        }




        dBinding.progressBar.visibility = View.VISIBLE
        val req = ServiceBuilder.buildService(TmdbEndpoints::class.java)
        val called = req.getDetails(id.toInt(), Constant.apiKey)
        val castCall = req.getCast(id.toInt(), Constant.apiKey)
        val reqNews = NewsBuilder.buildNewsService(TmdbEndpoints::class.java)


        called.enqueue(object : Callback<MovieDetails> {

            override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
                Log.d(TAG, "onResponse: Working till this point")
                if (response.isSuccessful) {
                    val movieDet = response.body()!!
                    Log.d(TAG, "onResponse: received response")
                    if (movieDet.overview.count() == 0) {
                        dBinding.overview2.text = "No Overview"
                    } else {
                        dBinding.overview2.text = movieDet.overview
                    }
                    dBinding.textView4.text = "Date:" + movieDet.release_date
                    dBinding.title2.text = movieDet.title
                    name = movieDet.title
                    dBinding.rating2.text =
                        "★: ${movieDet.vote_average}" + " (${movieDet.vote_count})"

                    dBinding.quoteMovie.isVisible = movieDet.tagline.isNotEmpty()
                    dBinding.quoteMovie.text = "'${movieDet.tagline}'"
                    themeImage = response.body()?.backdrop_path
                    posPath = movieDet.poster_path.toString()

                    val newsCall = reqNews.getNews("${movieDet.title} Movie", Constant.apiNewsKey3)

                    newsCall.enqueue(object : Callback<NewsHeadlinesData> {
                        override fun onResponse(
                            call: Call<NewsHeadlinesData>,
                            response: Response<NewsHeadlinesData>
                        ) {
                            if (response.isSuccessful) {
                                dBinding.newsRecyclerMovie.apply {
                                    setHasFixedSize(true)
                                    layoutManager = LinearLayoutManager(
                                        this@DetailsActivity,
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                                    adapter = NewsAdapter(response.body()!!)
                                }
                            }
                        }

                        override fun onFailure(call: Call<NewsHeadlinesData>, t: Throwable) {
                            Toast.makeText(this@DetailsActivity, "${t.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })

                    Glide.with(this@DetailsActivity)
                        .load("https://image.tmdb.org/t/p/w500${posPath}").into(dBinding.poster2)
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
                                dBinding.progressBar.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                dBinding.progressBar.visibility = View.GONE
                                return false
                            }
                        }).into(dBinding.imageView)


                    val mov = movieDet.id.toString()

                    val editor: SharedPreferences.Editor = sharedPreferences.edit()

                    val sharedValue = sharedPreferences.getString(movieDet.title, "notAdded")

                    dBinding.toggleFav.isChecked = sharedValue.equals(mov)


                    dBinding.toggleFav.setOnClickListener {
                        if (dBinding.toggleFav.isChecked) {
                            viewModel.insertMovie(FavouritesTable(mov))
                            editor.putString(movieDet.title, mov)
                            editor.apply()
                            editor.commit()
                        } else {
                            editor.remove(movieDet.title)
                            editor.apply()
                            editor.commit()
                            viewModel.deleteIdMovie(mov.toInt())
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

        dBinding.buttonSim.setOnClickListener {

            name?.let { it1 ->
                themeImage?.let { it2 ->
                    Navigator.similarMovies(
                        this,
                        id,
                        it1,
                        it2
                    )
                }
            }
        }

        dBinding.youtubeButton.setOnClickListener {
            val intentYT = Intent(this, MainActivity2::class.java)
            intentYT.putExtra(
                "movie_url",
                "https://www.youtube.com/results?search_query=$name trailer"
            )
            startActivity(intentYT)
        }

        dBinding.bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.review_item -> name?.let { it1 -> Navigator.seeReviews(this, id, it1) }

                R.id.home_item -> super.onBackPressed()

                R.id.share_item -> name?.let { it1 ->
                    posPath?.let { it2 ->
                        Navigator.shareButton(
                            this,
                            it1,
                            it2
                        )
                    }
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }
}

//    fun shareButton() {
//
//        val sh = Intent(Intent.ACTION_SEND)
//        sh.type = "text/plain"
//        sh.putExtra(
//            Intent.EXTRA_TEXT,
//            "Come watch '$name' with me: https://image.tmdb.org/t/p/w500$posPath"
//        )
//
//        val choose = Intent.createChooser(sh, "Share this movie")
//        startActivity(choose)
//
//    }

//    fun zoomImg() {
//        val i: Intent = Intent(this, enlarge::class.java)
//        i.putExtra("imgurl", posPath)
//        startActivity(i)
//
//    }

//    fun similarMovies() {
//        val intent = Intent(this, SimilarMoviesActivity::class.java)
//        intent.putExtra("id", id)
//        intent.putExtra("title", name)
//        intent.putExtra("themeImage", themeImage)
//        startActivity(intent)
//    }

//    fun seeReviews() {
//        val reviewIntent = Intent(this, ImageCollections::class.java)
//        reviewIntent.putExtra("id", id)
//        reviewIntent.putExtra("title", name)
//        startActivity(reviewIntent)
//    }
