package com.example.groceryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        val firstName : TextView = findViewById(R.id.firstNameField) as TextView
        val lastName : TextView = findViewById(R.id.userlastNamefield) as TextView
        val dateOfBirth : TextView = findViewById(R.id.userDateOfBirth) as TextView
        val phoneNmb : TextView = findViewById(R.id.userPhone) as TextView
        val zip : TextView = findViewById(R.id.userZip) as TextView
        val adress : TextView = findViewById(R.id.userAdress) as TextView
        val city : TextView = findViewById(R.id.userCity) as TextView

        val user = Firebase.auth.currentUser
        val uid = user?.uid
        var userName = ""
        var userLast = ""
        var userDate = ""
        var userPhone = ""
        var userZip = ""
        var userAdress = ""
        var userCity = ""

        db.collection("users").document("$uid")
            .get()
            .addOnSuccessListener { document ->
                userName = document.getString("First name") ?: "default"
                userLast = document.getString("Last name") ?: "default"
                userDate = document.getString("Date of birth") ?: "default"
                userPhone = document.getString("Phone number") ?: "default"
                userZip = document.getString("Zip Code") ?: "default"
                userAdress = document.getString("Adress") ?: "default"
                userCity = document.getString("City") ?: "default"
                firstName.setText(userName).toString()
                lastName.setText(userLast).toString()
                dateOfBirth.setText(userDate).toString()
                phoneNmb.setText(userPhone).toString()
                zip.setText(userZip).toString()
                adress.setText(userAdress).toString()
                city.setText(userCity).toString()
            }

    }

    private fun performUpdate() {
    //kolla om man kan köra en "reload" av sidan vid uppdatering så att oncreate körs igen

    }
}


























