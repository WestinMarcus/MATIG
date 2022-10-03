package com.example.groceryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val storesBtn = findViewById(R.id.btnStores) as Button
        //val userBtn = findViewById(R.id.userProfile) as Button

        storesBtn.setOnClickListener {
            val intent = Intent(this, StoresActivity::class.java)
            startActivity(intent)
        }

            /*
        userBtn.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }
            */
    }
}