package com.example.groceryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class settingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //BUTTONS
        val userBtn : TextView = findViewById(R.id.user_profile_TV)
        val notificationBtn  : TextView = findViewById(R.id.notification_tab_TV)
        val faqBtn : TextView = findViewById(R.id.faq_tab_TV)
        val signOutBtn: Button = findViewById(R.id.Signout_button_settings)
        val backBtn = findViewById(R.id.image_back_settings) as ImageView

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
        signOutBtn.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        backBtn.setOnClickListener {
            finish()
        }


    }
}