package com.example.groceryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var loginFailedText = findViewById(R.id.textView4) as TextView
        //get reference to button
        val login_clicker = findViewById(R.id.button) as Button
        login_clicker.setOnClickListener{
            loginFailedText.text = "Login Failed"
            Toast.makeText(this@MainActivity, "you clicked login.", Toast.LENGTH_SHORT).show()
        }
    }
}