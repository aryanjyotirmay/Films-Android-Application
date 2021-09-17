package com.aryanjyotirmay.films

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ViewPagerAdapter(private val movies: List<Result>) : RecyclerView.Adapter<ViewPagerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_pager, parent, false)
        return ViewPagerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        return holder.bind(movies[position])
    }
}

class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageVp = itemView.findViewById<ImageView>(R.id.image_vp)
        private val titleVp = itemView.findViewById<TextView>(R.id.title_vp)
    private val layoutVP = itemView.findViewById<ConstraintLayout>(R.id.layout_vp)

    fun bind(movie: Result) {
        Glide.with(itemView.context).load("https://image.tmdb.org/t/p/w500${movie.poster_path}").into(imageVp)

        titleVp.text = movie.title

        imageVp.setOnClickListener {
            if (movie.poster_path == null) {
                Toast.makeText(itemView.context, "No Data Available :(", Toast.LENGTH_SHORT).show()
            }

            if (movie.poster_path != null) {
                Toast.makeText(itemView.context, movie.title, Toast.LENGTH_SHORT).show()
                Log.d("TAG", "bind:Chal gaya ")
                val intent = Intent(itemView.context, DetailsActivity::class.java)
                intent.putExtra("imgapp", movie.poster_path)
                intent.putExtra("id", movie.id)
                itemView.context.startActivity(intent)
            }
        }
        }
}