package com.example.films

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface FavouritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: FavouritesTable)

    @Delete
    suspend fun delete(movie: FavouritesTable)

    @Query("select * from Favourites")
    fun getAllMovies(): LiveData<List<FavouritesTable>>
}