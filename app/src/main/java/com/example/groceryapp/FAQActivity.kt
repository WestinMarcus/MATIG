package com.example.groceryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView

class FAQActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faqactivity)

        val backBtn = findViewById<ImageView>(R.id.image_back_settings)

        backBtn.setOnClickListener {
            finish()
        }
        val arrayAdapter: ArrayAdapter<*>
        val faqListView = findViewById<ListView>(R.id.lv_faq)
        val questionsAndAnswers = mutableListOf<String>()

        // --------- Account questions ------------
        questionsAndAnswers.add("How do i create an account?")
        questionsAndAnswers.add("Do i need an account?")
        questionsAndAnswers.add("How do i change password?")
        // --------- Product questions -------------
        questionsAndAnswers.add("When are the deals updated?")
        questionsAndAnswers.add("Which stores are used in the app?")
        questionsAndAnswers.add("How do i find the best price for a product?")
        // -------- placeholder questions -----------
        questionsAndAnswers.add("Can I add my own products to the shoppinglist?")
        questionsAndAnswers.add("How do I create/ delete a shoppinglist?")
        questionsAndAnswers.add("Can I see products I've purchased in the past?")


        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, questionsAndAnswers)
        faqListView.adapter = arrayAdapter



    }
}