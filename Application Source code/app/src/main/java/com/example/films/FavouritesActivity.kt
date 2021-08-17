package com.example.films

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_favourites.*

class FavouritesActivity : AppCompatActivity() {
    lateinit var viewModel: MoviesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        fav_recycler.layoutManager = GridLayoutManager(this@FavouritesActivity,2)
        val adapter = FavAdapter(this,this)
        fav_recycler.adapter = adapter

        viewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(MoviesViewModel::class.java)

        viewModel.allMovies.observe(this, Observer {list -> list?.let {
            adapter.updateList(it)
        }

        })

    }

    fun onItemClicked(movie: FavouritesTable) {
        viewModel.deleteMovie(movie)
        Toast.makeText(this,"Removed From Favourites",Toast.LENGTH_SHORT).show()
    }

}