package com.example.groceryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val storesBtn = findViewById(R.id.btn_search) as ImageButton
        val userBtn = findViewById(R.id.btn_user) as ImageButton

        storesBtn.setOnClickListener {
            val intent = Intent(this, StoresActivity::class.java)
            startActivity(intent)
        }


        userBtn.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

    }
}