package com.example.groceryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val loginFailedText = findViewById(R.id.textView4) as TextView
        //get reference to button
        val loginBtn = findViewById(R.id.button) as Button
        loginBtn.setOnClickListener{
            loginFailedText.text = "Login Failed"
            Toast.makeText(this@MainActivity, "you clicked login.", Toast.LENGTH_SHORT).show()

        }
        val createUsrBtn = findViewById(R.id.button2) as Button
        createUsrBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}