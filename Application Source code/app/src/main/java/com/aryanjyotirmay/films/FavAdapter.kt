package com.aryanjyotirmay.films

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavAdapter(private val context: Context,  val listener: FavouritesActivity) :
    RecyclerView.Adapter<FavAdapter.FavViewHolder>() {

    private val allMovies = ArrayList<FavouritesTable>()
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("movieFavourites",Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sharedPreferences.edit()

    inner class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.fav_pos)
        val favTitle: TextView = itemView.findViewById(R.id.fav_title)
        val favRat: TextView = itemView.findViewById(R.id.fav_rat)
        val deleteButton: ImageButton = itemView.findViewById(R.id.fav_del)
        val reviewButton: ImageButton = itemView.findViewById(R.id.fav_review)
        val proBar: ProgressBar = itemView.findViewById(R.id.fav_progress)
        val itemLayout: ConstraintLayout = itemView.findViewById(R.id.item_fav_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {

        return FavViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_favourites, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val currentNote = allMovies[position]
        val id = currentNote.movieID

        val req = ServiceBuilder.buildService(TmdbEndpoints::class.java)
        val called = req.getDetails(id.toInt(), Constant.apiKey)
        holder.proBar.visibility = View.VISIBLE


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
                    holder.deleteButton.setOnClickListener {
                        listener.onItemClicked(allMovies[holder.absoluteAdapterPosition])
                        editor.remove(movieDet.title)
                        editor.apply()
                        editor.commit()
                    }
                    holder.favRat.text = "⭐ "+movieDet.vote_average.toString()
                    holder.reviewButton.setOnClickListener {
                        Navigator.seeReviews(context,movieDet.id.toString(),movieDet.title)
                    }
                    Glide.with(context)
                        .load("https://image.tmdb.org/t/p/w500${movieDet.poster_path}").listener(object: RequestListener<Drawable>{
                            override fun onLoadFailed (
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.proBar.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                holder.proBar.visibility = View.GONE
                                return false
                            }
                        })
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

