package com.example.films

import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MoviesAdapter(private val movies: List<Result>) : RecyclerView.Adapter<MoviesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MoviesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        return holder.bind(movies[position])
    }
}

class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val bar: ProgressBar = itemView.findViewById(R.id.loading)
    private val photo: ImageView = itemView.findViewById(R.id.movie_photo)
    private val title: TextView = itemView.findViewById(R.id.movie_title)
    private val rating: TextView = itemView.findViewById(R.id.movie_rating)
    private val parentLayout: RelativeLayout = itemView.findViewById(R.id.parent_layout)

    fun bind(movie: Result) {
        bar.visibility = View.VISIBLE
        val drawable: Drawable? = ResourcesCompat.getDrawable(
            itemView.resources,
            R.drawable._98_2987494_png_file_svg_movie_filming_icon_png_transparent_png,
            null
        )

        Glide.with(itemView.context).load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
            .error(drawable)
            .listener(object : RequestListener<Drawable> {
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
            }).into(photo)

        title.text = movie.title
        rating.text = "⭐ " + movie.vote_average.toString()

        parentLayout.setOnClickListener {
            if (movie.poster_path == null) {
                Toast.makeText(itemView.context, "No Data Available :(", Toast.LENGTH_SHORT).show()
            }
            if (movie.poster_path != null) {
                Log.d("TAG", "bind:Chal gaya ")
                val intent = Intent(itemView.context, DetailsActivity::class.java)
                intent.putExtra("imgapp", movie.poster_path)
                intent.putExtra("id", movie.id)
                itemView.context.startActivity(intent)
            }
        }
    }
}