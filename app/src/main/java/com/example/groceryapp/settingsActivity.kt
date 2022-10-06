package com.example.groceryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class settingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val userBtn : TextView = findViewById(R.id.user_profile_TV)
        val notificationBtn  : TextView = findViewById(R.id.notification_tab_TV)
        val faqBtn : TextView = findViewById(R.id.faq_tab_TV)

        userBtn.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }
        notificationBtn.setOnClickListener {
            //Länka till notification activity
        }
        faqBtn.setOnClickListener {
            //Länka till faq activity
        }


    }
}