package com.example.films

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbEndpoints {

    @GET("/3/movie/popular")
    fun getMovies(@Query("api_key") key: String): retrofit2.Call<PopularMovies>

    @GET("/3/movie/top_rated")
    fun getMoviesTopRated(@Query("api_key") key: String): retrofit2.Call<PopularMovies>

    @GET("/3/movie/upcoming")
    fun getMoviesUpcoming(@Query("api_key") key: String): retrofit2.Call<PopularMovies>

    @GET("/3/movie/{id}")
    fun getDetails(@Path("id") id: Int, @Query("api_key") key: String): retrofit2.Call<MovieDetails>

    @GET("/3/movie/{id}/similar")
    fun getSimilar(
        @Path("id") id: Int,
        @Query("api_key") key: String
    ): retrofit2.Call<PopularMovies>

    @GET("/3/search/movie")
    fun searchMovie(
        @Query("api_key") key: String,
        @Query("query") movieName: String
    ): retrofit2.Call<PopularMovies>

    @GET("/3/movie/{id}/credits")
    fun getCast(@Path("id") id: Int, @Query("api_key") key: String): retrofit2.Call<CastData>

    @GET("/3/movie/{id}/reviews")
    fun getReviews(@Path("id") id: Int, @Query("api_key") key: String): retrofit2.Call<ReviewData>

    @GET("/3/person/{id}")
    fun getActor(@Path("id") id: Int, @Query("api_key") key: String): retrofit2.Call<DataActor>

    @GET("/3/person/{id}/movie_credits")
    fun getMoviesActor(
        @Path("id") id: Int,
        @Query("api_key") key: String
    ): retrofit2.Call<DataActorMovies>


}