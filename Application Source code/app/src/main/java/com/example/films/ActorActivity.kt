package com.example.films

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_actor.*
import kotlinx.android.synthetic.main.activity_similar_movies.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActorActivity : AppCompatActivity() {
    private var posActor:String?=null
    private var nameShare:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actor)

        val idActor = intent?.extras?.getInt("ActorId").toString()
        val req = ServiceBuilder.buildService(TmdbEndpoints::class.java)
       val callActor = req.getActor(idActor.toInt(),Constant.apiKey)

        val callActorMovies = req.getMoviesActor(idActor.toInt(),Constant.apiKey)


        val nameA: TextView =findViewById(R.id.actor_name)
        val birthA:TextView=findViewById(R.id.actor_birth)
        val photoA: ImageView =findViewById(R.id.actor_image)
        val back:ImageView=findViewById(R.id.actor_imageView)
        val knownFor:TextView=findViewById(R.id.actor_knownfor)
        val barz:ProgressBar=findViewById(R.id.progressBarA)

        val buttonHome:ImageButton=findViewById(R.id.button2)
        buttonHome.setOnClickListener{ startActivity(Intent(this,MainActivity::class.java))}

        val detailA:TextView=findViewById(R.id.actor_overview)
        detailA.movementMethod= ScrollingMovementMethod()

        barz.visibility= View.VISIBLE

        callActor.enqueue(object: Callback<DataActor> {
            override fun onResponse(call: Call<DataActor>, response: Response<DataActor>) {
                if (response.isSuccessful){
                    val actorDetail=response.body()!!
                    nameA.text=actorDetail.name
                    nameShare=actorDetail.name
                    birthA.text=actorDetail.birthday
                    if(actorDetail.biography.count()!=0) {
                        detailA.text = actorDetail.biography
                    }
                    if (actorDetail.biography.count()==0){
                        detailA.text="No information available"
                    }
                    knownFor.text=actorDetail.known_for_department
                    posActor = actorDetail.profile_path

                    Glide.with(this@ActorActivity).load("https://image.tmdb.org/t/p/w500${posActor}").error(R.drawable.images_2).into(photoA)
                    Glide.with(this@ActorActivity).load("https://image.tmdb.org/t/p/w500${posActor}").listener(object: RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            barz.visibility=View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            barz.visibility=View.GONE
                            return false
                        }

                    }

                    ).into(back)




                }
            }

            override fun onFailure(call: Call<DataActor>, t: Throwable) {
                Toast.makeText(this@ActorActivity,"${t.message}",Toast.LENGTH_SHORT).show()
            }
        })

        callActorMovies.enqueue(object : Callback<DataActorMovies> {
            override fun onResponse(call: Call<DataActorMovies>, response: Response<DataActorMovies>) {
                if (response.isSuccessful) {

                    actor_recycler.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@ActorActivity,LinearLayoutManager.HORIZONTAL,false)
                        adapter = ActorMoviesAdapter(response.body()!!.cast)
                    }

                }

            }

            override fun onFailure(call: Call<DataActorMovies>, t: Throwable) {
                Toast.makeText(this@ActorActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })


    }

    fun zoomImgA(view: View) {
        if (posActor!=null) {
            val intA = Intent(this, enlarge::class.java)
            intA.putExtra("imgurl", posActor)
            startActivity(intA)
        }
        if (posActor==null){
            Toast.makeText(this,"No Image :(",Toast.LENGTH_SHORT).show()
        }


    }

    fun shareButtonA(view: View) {

        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_TEXT,"Hey! Check ${nameShare} out: https://image.tmdb.org/t/p/w500${posActor}")

        val chooser = Intent.createChooser(share,"Share Actor")
        if(posActor!=null){
        startActivity(chooser)}
        if(posActor==null){
            Toast.makeText(this,"No data to share",Toast.LENGTH_SHORT).show()
        }

    }
}