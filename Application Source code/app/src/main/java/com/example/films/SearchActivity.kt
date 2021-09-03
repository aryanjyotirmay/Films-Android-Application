package com.example.films

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_similar_movies.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_similar_movies)

        val searchTerm = intent?.extras?.getString("search").toString()

        val req = ServiceBuilder.buildService(TmdbEndpoints::class.java)
        val callSearch = req.searchMovie(Constant.apiKey, searchTerm)
        val header: TextView = findViewById(R.id.headerText)
        header.text = "Search results for: ${searchTerm}"

        callSearch.enqueue(object : Callback<PopularMovies> {
            override fun onResponse(call: Call<PopularMovies>, response: Response<PopularMovies>) {

                if (response.isSuccessful) {
                    recyclerViewSimilar.apply {
                        setHasFixedSize(true)
                        layoutManager = GridLayoutManager(this@SearchActivity, 2)
                        adapter = MoviesAdapter(response.body()!!.results)
                    }

                }
                if (response.body()!!.results.count() == 0) {
                    val drawable: Drawable? = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.screenshot_2021_06_18_at_22_31_12,
                        null
                    )
                    Glide.with(this@SearchActivity).load(drawable).into(backImage)
                }
            }

            override fun onFailure(call: Call<PopularMovies>, t: Throwable) {
                Toast.makeText(this@SearchActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.menu_items, menu)

        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searchMov = menu?.findItem(R.id.search_item)

        val searchView = searchMov?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // searchView.clearFocus()
                searchView.setQuery("", false)
                searchView.onActionViewCollapsed()
                Navigator.searchHandler(this@SearchActivity,query.toString())
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
            val voiceText = array[0]
            Navigator.searchHandler(this@SearchActivity,voiceText)
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }
}