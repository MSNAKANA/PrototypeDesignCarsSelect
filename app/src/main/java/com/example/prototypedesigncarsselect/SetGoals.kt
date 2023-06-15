package com.example.prototypedesigncarsselect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar

class SetGoals : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_goals)

        val numcars = findViewById<ProgressBar>(R.id.numcars)
        val category1 = findViewById<EditText>(R.id.category1)
        val setgoalsbtn = findViewById<Button>(R.id.setgoalsbtn)

        numcars.setOnClickListener{
            val progress = category1.text.toString().toIntOrNull()

            if(progress != null && progress in 0..100){
               numcars.progress = progress

        }else{
            // handle invalid input
        }


        }
    }
}