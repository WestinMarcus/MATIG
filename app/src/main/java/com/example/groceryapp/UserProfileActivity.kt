package com.example.groceryapp

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase



class UserProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val db = Firebase.firestore

        //Button vals
        val backBtn: ImageView = findViewById(R.id.image_back)
        val updateBtn: Button = findViewById(R.id.btn_update)
        val signOutBtn: Button = findViewById(R.id.Signout)



        // Buttonevents
        backBtn.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        updateBtn.setOnClickListener{
            performUpdate()
        }
        signOutBtn.setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Tar in textview
        var textView : TextView = findViewById(R.id.firstNameField) as TextView

        //Sätter nytt värde på textview
        textView.setText(getUserName()).toString()
    }

    private fun getUserName(): String {
        
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        var userName = ""

        db.collection("users").document("$uid")
            .get()
            .addOnSuccessListener { document ->
                userName = document.getString("Adress") ?: "default"
            }

        return userName
    }

    private fun performUpdate() {


    }
}


























