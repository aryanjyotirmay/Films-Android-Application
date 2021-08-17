package com.example.films

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavAdapter(private val context: Context, private val listener: FavouritesActivity) :
    RecyclerView.Adapter<FavAdapter.FavViewHolder>() {

    val allMovies = ArrayList<FavouritesTable>()

    inner class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster = itemView.findViewById<ImageView>(R.id.fav_pos)
        val favTitle = itemView.findViewById<TextView>(R.id.fav_title)
        val favRat = itemView.findViewById<TextView>(R.id.fav_rat)
        val deleteButton = itemView.findViewById<Button>(R.id.fav_del)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {

        val viewHolder = FavViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_favourites, parent, false)
        )
        viewHolder.deleteButton.setOnClickListener {
            listener.onItemClicked(allMovies[viewHolder.absoluteAdapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val currentNote = allMovies[position]
        val id = currentNote.movieID

        val req = ServiceBuilder.buildService(TmdbEndpoints::class.java)
        val called = req.getDetails(id.toInt(), Constant.apiKey)

        called.enqueue(object : Callback<MovieDetails> {
            override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
                if (response.isSuccessful) {
                    val movieDet = response.body()!!
                    holder.poster.setOnClickListener {
                        val intent = Intent(context, DetailsActivity::class.java)
                        intent.putExtra("id", movieDet.id)
                        context.startActivity(intent)
                    }

                    holder.favTitle.text = movieDet.title
                    holder.favRat.text = "⭐ "+movieDet.vote_average.toString()
                    Glide.with(context)
                        .load("https://image.tmdb.org/t/p/w500${movieDet.poster_path}")
                        .into(holder.poster)
                }
            }

            override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })


    }

    override fun getItemCount(): Int {
        return allMovies.size
    }

    fun updateList(newList: List<FavouritesTable>) {
        allMovies.clear()
        allMovies.addAll(newList)

        notifyDataSetChanged()
    }


}

interface INotesRVAdapter {
    fun onItemClicked(movie: FavouritesTable)
}

