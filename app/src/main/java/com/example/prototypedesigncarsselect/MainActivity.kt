package com.example.prototypedesigncarsselect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button_siU = findViewById<Button>(R.id.button_siU)
        button_siU.setOnClickListener {
            val intent = Intent(this, CarsSelectSignUp::class.java)
            startActivity(intent)
        }
        val button_log = findViewById<Button>(R.id.button_log)
        button_log.setOnClickListener {
            val intent = Intent(this, Login2::class.java)
            startActivity(intent)

        }
    }
}

//ST10102523 kabelo Malema
//ST10106472 Mohlala Nakana