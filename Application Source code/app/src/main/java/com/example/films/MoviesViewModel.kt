package com.example.films

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesViewModel(application: Application) : AndroidViewModel(application) {
    val allMovies: LiveData<List<FavouritesTable>>
    private val repository: MoviesRepository

    init {
        val dao = MoviesDatabase.getDatabase(application).getMoviesDao()
        repository = MoviesRepository(dao)
        allMovies = repository.allMovies()
    }

    fun deleteMovie(movie: FavouritesTable) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(movie)
    }

    fun insertMovie(movie: FavouritesTable) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(movie)
    }
}