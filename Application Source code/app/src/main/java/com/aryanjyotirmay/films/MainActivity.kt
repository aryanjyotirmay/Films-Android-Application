package com.aryanjyotirmay.films

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bekawestberg.loopinglayout.library.LoopingLayoutManager
import com.aryanjyotirmay.films.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var voiceText: String
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val request = ServiceBuilder.buildService(TmdbEndpoints::class.java)

        val newsRequest = NewsBuilder.buildNewsService(TmdbEndpoints::class.java)

        val call = request.getMovies(Constant.apiKey)
        val call2 = request.getMoviesTopRated(Constant.apiKey)
        val call3 = request.getMoviesUpcoming(Constant.apiKey)
        val callNews = newsRequest.getTopMovieNews("in","entertainment",Constant.apiNewsKey)
        val callNewsInt = newsRequest.getTopMovieNews("us","entertainment",Constant.apiNewsKey)


        callNews.enqueue(object: Callback<NewsHeadlinesData> {
            override fun onResponse(
                call: Call<NewsHeadlinesData>,
                response: Response<NewsHeadlinesData>
            ) {
                if (response.isSuccessful) {
                    binding.newsRecycler.apply {
                        setHasFixedSize(true)
                        layoutManager = LoopingLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                        adapter = NewsAdapter(response.body()!!)
                    }
                }
            }

            override fun onFailure(call: Call<NewsHeadlinesData>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })

        callNewsInt.enqueue(object: Callback<NewsHeadlinesData> {
            override fun onResponse(
                call: Call<NewsHeadlinesData>,
                response: Response<NewsHeadlinesData>
            ) {
                if (response.isSuccessful) {
                    binding.newsRecyclerInt.apply {
                        setHasFixedSize(true)
                        layoutManager = LoopingLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                        adapter = NewsAdapter(response.body()!!)
                    }
                }
            }

            override fun onFailure(call: Call<NewsHeadlinesData>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })

        call.enqueue(object : Callback<PopularMovies> {

            override fun onResponse(call: Call<PopularMovies>, response: Response<PopularMovies>) {

                if (response.isSuccessful) {

                    recyclerView.apply {
                        setHasFixedSize(true)
                        layoutManager = LoopingLayoutManager(
                            this@MainActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        adapter = MoviesAdapter(response.body()!!.results)
                    }
                }
            }

            override fun onFailure(call: Call<PopularMovies>, t: Throwable) {

                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()

            }
        })
        call2.enqueue(object : Callback<PopularMovies> {
            override fun onResponse(call: Call<PopularMovies>, response: Response<PopularMovies>) {
                Log.d(TAG, "onResponse:before if loop ")
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: inside if loop")
                    recyclerViewUp.apply {
                        setHasFixedSize(true)
                        layoutManager = LoopingLayoutManager(
                            this@MainActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        adapter = MoviesAdapter(response.body()!!.results)
                    }

                }
            }

            override fun onFailure(call: Call<PopularMovies>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
        call3.enqueue(object : Callback<PopularMovies> {
            override fun onResponse(call: Call<PopularMovies>, response: Response<PopularMovies>) {
                if (response.isSuccessful) {

                    recyclerViewLat.apply {
                        setHasFixedSize(true)
                        layoutManager = LoopingLayoutManager(
                            this@MainActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        adapter = MoviesAdapter(response.body()!!.results)
                    }
                    view_pager.adapter = ViewPagerAdapter(response.body()!!.results)

                }
            }

            override fun onFailure(call: Call<PopularMovies>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        //val inflater = menuInflater
        menuInflater.inflate(R.menu.menu_items, menu)

        val searchView = menu?.findItem(R.id.search_item)?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchView.onActionViewCollapsed()
                Navigator.searchHandler(this@MainActivity,query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.voice_search -> {
                voiceSearch()
            }

            R.id.favourites -> {
                val iFav = Intent(this, FavouritesActivity::class.java)
                startActivity(iFav)
            }
            R.id.nearby_movie -> {
                val iMap = Intent(this,MapsActivity::class.java)
                startActivity(iMap)
            }
        }
        return true
    }

    private fun voiceSearch() {
        val voiceIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        voiceIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        startActivityIfNeeded(voiceIntent, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == RESULT_OK) {
            val array =
                ArrayList<String>(data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS))
            voiceText = array[0]
            Navigator.searchHandler(this@MainActivity,voiceText)
        } else {
            Toast.makeText(this, "Voice Search Terminated", Toast.LENGTH_SHORT).show()
        }
    }
}