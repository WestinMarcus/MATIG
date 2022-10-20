package com.example.groceryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class notificationsSwitchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications_switch)

        var isChecked = false
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        val uid = user?.uid

        val acceptNotis = findViewById<Switch>(R.id.switch2)
        val backBtn = findViewById<ImageView>(R.id.image_back_settings)

        backBtn.setOnClickListener {
            finish()
        }


        // Funktion för att tanka ner och fylla ut användardata
        db.collection("users").document("$uid")
            .get()
            .addOnSuccessListener { document ->
                isChecked = (document.getBoolean("Notiser") ?: "default") as Boolean
                acceptNotis.isChecked = isChecked

                acceptNotis.setOnClickListener(){
                    val userData = document.data

                    if(acceptNotis.isChecked){
                        userData?.put("Notiser", true)
                        if (userData != null) {
                            db.collection("users").document("$uid")
                                .set(userData)
                        }
                    }else{
                        userData?.put("Notiser", false)
                        if (userData != null) {
                            db.collection("users").document("$uid")
                                .set(userData)
                        }
                    }
                }

            }
    }
}