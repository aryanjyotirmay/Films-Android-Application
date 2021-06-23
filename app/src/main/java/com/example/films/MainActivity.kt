package com.example.films

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bekawestberg.loopinglayout.library.LoopingLayoutManager
import com.example.films.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        Log.d(TAG, "onCreate: hereAryan")

        val request = ServiceBuilder.buildService(TmdbEndpoints::class.java)
        val call = request.getMovies(Constant.apiKey)

        val call2 = request.getMoviesTopRated(Constant.apiKey)

        val call3 = request.getMoviesUpcoming(Constant.apiKey)
        call.enqueue(object : Callback<PopularMovies> {
            override fun onResponse(call: Call<PopularMovies>, response: Response<PopularMovies>) {
                if (response.isSuccessful) {

                    recyclerView.apply {
                        setHasFixedSize(true)
                        layoutManager = LoopingLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
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
                        layoutManager = LoopingLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
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
                        layoutManager = LoopingLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                        adapter = MoviesAdapter(response.body()!!.results)
                    }

                }
            }

            override fun onFailure(call: Call<PopularMovies>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })


        binding.button?.setOnClickListener{

            val searchTerm = binding.editText?.text.toString()
            if(searchTerm.count()!=0){
            val i = Intent(this,SearchActivity::class.java)
            i.putExtra("search",searchTerm)
            startActivity(i)}
        }


        binding.editText?.setOnKeyListener{ view, keyCode, _ -> handleKeyEvent(view,keyCode)}


    }
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }



}