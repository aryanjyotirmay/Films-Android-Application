package com.example.films

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class BossActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boss)
        val buttonB: Button = findViewById(R.id.button_aryan)

        buttonB.setOnClickListener {

            startActivity(Intent(this, MainActivity::class.java))
        }


    }


}