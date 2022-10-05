package com.example.groceryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import android.widget.ToggleButton

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val button_toggle = findViewById(R.id.btn_toggle) as ToggleButton
        var searchToggle = ""
        button_toggle.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked)
                searchToggle = "Products"
            else
                searchToggle = "Stores"
            Toast.makeText(this, searchToggle, Toast.LENGTH_SHORT).show()
        }
    }
}