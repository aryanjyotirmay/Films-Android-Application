package com.example.films

import androidx.lifecycle.LiveData

class MoviesRepository(private val moviesDao: FavouritesDao) {

    fun allMovies(): LiveData<List<FavouritesTable>> = moviesDao.getAllMovies()

    suspend fun insert(movie: FavouritesTable) {
        moviesDao.insert(movie)
    }

    suspend fun delete(movie: FavouritesTable) {
        moviesDao.delete(movie)
    }

}