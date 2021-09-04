package com.example.films

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_favourites.*

class FavouritesActivity : AppCompatActivity() {
    lateinit var viewModel: MoviesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)


        val adapter = FavAdapter(this, this)
        fav_recycler.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this@FavouritesActivity)
        }

        no_fav.visibility = View.VISIBLE
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(MoviesViewModel::class.java)

        viewModel.allMovies.observe(this, Observer { list ->
            list?.let {
                if (list.count() != 0) {
                    fav_recycler.visibility = View.VISIBLE
                    fav_header.visibility = View.VISIBLE
                    no_fav.visibility = View.GONE
                    adapter.updateList(it)
                }
                if (list.count() == 0) {
                    no_fav.visibility = View.VISIBLE
                    fav_recycler.visibility = View.GONE
                    fav_header.visibility = View.GONE
                }
            }

        })

    }

    fun onItemClicked(movie: FavouritesTable) {
        viewModel.deleteMovie(movie)
        Toast.makeText(this, "Removed From Favourites", Toast.LENGTH_SHORT).show()
    }

}