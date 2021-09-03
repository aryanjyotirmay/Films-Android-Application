package com.example.films

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favourites")
class FavouritesTable(@ColumnInfo(name = "movieData") val movieID: String) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}