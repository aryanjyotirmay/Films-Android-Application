package com.aryanjyotirmay.films

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(FavouritesTable::class), version = 1, exportSchema = false)
abstract class MoviesDatabase() : RoomDatabase() {

    abstract fun getMoviesDao(): FavouritesDao

    companion object {
        @Volatile
        private var INSTANCE: MoviesDatabase? = null

        fun getDatabase(context: Context): MoviesDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoviesDatabase::class.java,
                    "Favourites"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}