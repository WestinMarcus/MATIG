package com.example.groceryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class PopOutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_out)
        val product = intent.getStringExtra("product")
        val productHeader: TextView = findViewById(R.id.tv_productName)
        productHeader.text = "$product"

    }
}