package com.aryanjyotirmay.films
import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface FavouritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: FavouritesTable)

    @Delete
    suspend fun delete(movie: FavouritesTable)

    @Query("Delete From Favourites Where movieData = :movieId")
    fun deleteMovieById(movieId: Int)

//    @Query("Select movieData From Favourites")
//    fun getMovieList(): List<String>

    @Query("select * from Favourites")
    fun getAllMovies(): LiveData<List<FavouritesTable>>
}