package com.example.groceryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ItemListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)
        val store = intent.getStringExtra("arg");
        val storeHeader: TextView = findViewById(R.id.tvStoreItemList)
        storeHeader.text = "$store"
    }
}