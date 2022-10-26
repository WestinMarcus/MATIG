package com.example.groceryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView

class FAQActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faqactivity)

        val backBtn = findViewById<ImageView>(R.id.image_back_settings)
        backBtn.setOnClickListener {
            finish()
        }

        val q1Tv = findViewById<TextView>(R.id.tv_question1)
        val q2Tv = findViewById<TextView>(R.id.tv_question2)
        val q3Tv = findViewById<TextView>(R.id.tv_question3)

        val a1Tv = findViewById<TextView>(R.id.tv_answer1)
        val a2Tv = findViewById<TextView>(R.id.tv_answer2)
        val a3Tv = findViewById<TextView>(R.id.tv_answer3)

        //------------------ questions -------------------
        val q1 = "When are the deals updated?"
        val q2 = "Can I see products I've purchased in the past?"
        val q3 = "How do i change my password?"
        //------------------- answers --------------------
        val a1 = "Every Monday at 8am"
        val a2 = "Yes you can! Click the Settings button in the Navigation bar -> Purchase History button"
        val a3 = "Click the Settings button in the Navigation bar -> User Profile button -> Change Password. Proceed and follow the instructions in the email sent to your registered email address."

        q1Tv.setText(q1)
        a1Tv.setText(a1)
        q2Tv.setText(q2)
        a2Tv.setText(a2)
        q3Tv.setText(q3)
        a3Tv.setText(a3)



    }
}