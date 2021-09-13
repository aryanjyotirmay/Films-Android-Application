package com.example.films

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.films.databinding.ActivityActorBinding
import kotlinx.android.synthetic.main.activity_actor.*
import kotlinx.android.synthetic.main.activity_similar_movies.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActorActivity : AppCompatActivity() {
    private var posActor: String? = null
    private var nameShare: String? = null
    private lateinit var binding: ActivityActorBinding
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idActor = intent?.extras?.getInt("ActorId").toString()
        val req = ServiceBuilder.buildService(TmdbEndpoints::class.java)
        val callActor = req.getActor(idActor.toInt(), Constant.apiKey)

        val callActorMovies = req.getMoviesActor(idActor.toInt(), Constant.apiKey)

        val reqActor = NewsBuilder.buildNewsService(TmdbEndpoints::class.java)



        binding.actorImage.setOnClickListener {
            posActor?.let { it1 -> Navigator.zoomImg(this, it1) }
        }

//        val nameA: TextView = findViewById(R.id.actor_name)
//        val birthA: TextView = findViewById(R.id.actor_birth)
//        val photoA: ImageView = findViewById(R.id.actor_image)
//        val back: ImageView = findViewById(R.id.actor_imageView)
//        val knownFor: TextView = findViewById(R.id.actor_knownfor)
//        val barz: ProgressBar = findViewById(R.id.progressBarA)
//        val buttonHome: ImageButton = findViewById(R.id.button2)
//        val detailA: TextView = findViewById(R.id.actor_overview)

        binding.actorOverview.movementMethod = ScrollingMovementMethod()
        binding.actorOverview.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN ->
                    v.parent.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_UP ->
                    v.parent.requestDisallowInterceptTouchEvent(false)
            }
            false
        }

        binding.progressBarA.visibility = View.VISIBLE

        callActor.enqueue(object : Callback<DataActor> {
            override fun onResponse(call: Call<DataActor>, response: Response<DataActor>) {
                if (response.isSuccessful) {
                    val actorDetail = response.body()!!
                    binding.actorName.text = actorDetail.name
                    nameShare = actorDetail.name
                    binding.actorBirth.text = actorDetail.birthday
                    if (actorDetail.biography.count() != 0) {
                        binding.actorOverview.text = actorDetail.biography
                    }
                    if (actorDetail.biography.count() == 0) {
                        binding.actorOverview.text = "No information available"
                    }
                    binding.actorKnownfor.text = actorDetail.known_for_department
                    posActor = actorDetail.profile_path

                    Glide.with(this@ActorActivity)
                        .load("https://image.tmdb.org/t/p/w500${posActor}")
                        .error(R.drawable.images_2).into(binding.actorImage)
                    Glide.with(this@ActorActivity)
                        .load("https://image.tmdb.org/t/p/w500${posActor}")
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding.progressBarA.visibility = View.GONE
                                return false
                            }
                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding.progressBarA.visibility = View.GONE
                                return false
                            }
                        }).into(binding.actorImageView)

                    val callActorNews = reqActor.getNews(actorDetail.name,Constant.apiNewsKey2)

                    callActorNews.enqueue(object: Callback<NewsHeadlinesData>{
                        override fun onResponse(
                            call: Call<NewsHeadlinesData>,
                            response: Response<NewsHeadlinesData>
                        ) {
                            if (response.isSuccessful) {
                                binding.newsRecyclerActor.apply {
                                    setHasFixedSize(true)
                                    layoutManager = LinearLayoutManager(this@ActorActivity,LinearLayoutManager.HORIZONTAL,false)
                                    adapter = NewsAdapter(response.body()!!)
                                }
                            }
                        }

                        override fun onFailure(call: Call<NewsHeadlinesData>, t: Throwable) {
                            Toast.makeText(this@ActorActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                        }

                    })

                }
            }

            override fun onFailure(call: Call<DataActor>, t: Throwable) {
                Toast.makeText(this@ActorActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        callActorMovies.enqueue(object : Callback<DataActorMovies> {
            override fun onResponse(
                call: Call<DataActorMovies>,
                response: Response<DataActorMovies>
            ) {
                if (response.isSuccessful) {
                    actor_recycler.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(
                            this@ActorActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        adapter = ActorMoviesAdapter(response.body()!!.cast)
                    }
                }
            }

            override fun onFailure(call: Call<DataActorMovies>, t: Throwable) {
                Toast.makeText(this@ActorActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
     //   val reviewItem=binding.bottomNavActor[R.id.review_item]
        binding.bottomNavActor.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_item -> super.onBackPressed()
                R.id.share_item ->             posActor?.let { it1 ->
                    nameShare?.let { it2 ->
                        Navigator.shareButtonA(
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

//    fun zoomImgA() {
//        if (posActor != null) {
//            val intA = Intent(this, enlarge::class.java)
//            intA.putExtra("imgurl", posActor)
//            startActivity(intA)
//        }
//        if (posActor == null) {
//            Toast.makeText(this, "No Image :(", Toast.LENGTH_SHORT).show()
//        }
//
//
//    }
//    fun shareButtonA() {
//
//        val share = Intent(Intent.ACTION_SEND)
//        share.type = "text/plain"
//        share.putExtra(
//            Intent.EXTRA_TEXT,
//            "Hey! Check ${nameShare} out: https://image.tmdb.org/t/p/w500${posActor}"
//        )
//
//        val chooser = Intent.createChooser(share, "Share Actor")
//        if (posActor != null) {
//            startActivity(chooser)
//        }
//
//        if (posActor == null) {
//            Toast.makeText(this, "No data to share", Toast.LENGTH_SHORT).show()
//        }
//
//    }
