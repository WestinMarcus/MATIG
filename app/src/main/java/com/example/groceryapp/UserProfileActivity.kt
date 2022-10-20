package com.example.groceryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class UserProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val db = Firebase.firestore

        //Button vals
        val backBtn: ImageView = findViewById(R.id.image_back_settings)
        val updateBtn: Button = findViewById(R.id.btn_update)
        val passReset: TextView = findViewById(R.id.pass)
        //val emailReset: TextView = findViewById(R.id.emailReset)

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
        val userEmail = user?.email

        var userName = ""
        var userLast = ""
        var userDate = "Fixas senare"
        var userPhone = ""
        var userZip = ""
        var userAdress = ""
        var userCity = ""

        // Buttonevents
        backBtn.setOnClickListener{
            finish()
        }
        updateBtn.setOnClickListener{
            performUpdate()
        }

        //Button that sends password reset email to the current users email
        passReset.setOnClickListener{
            val email= userEmail.toString()
            if (email.isEmpty()){
                Toast.makeText(this, "Please enter email adress", Toast.LENGTH_SHORT).show()
            }else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Email sent successfully to reset your password!",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }else{
                            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        /*emailReset.setOnClickListener {

        }*/


        // Funktion för att tanka ner och fylla ut användardata
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
        val db = Firebase.firestore

        val firstName : TextView = findViewById(R.id.firstNameField) as TextView
        val lastName : TextView = findViewById(R.id.userlastNamefield) as TextView
        val dateOfBirth : TextView = findViewById(R.id.userDateOfBirth) as TextView
        val phoneNmb : TextView = findViewById(R.id.userPhone) as TextView
        val zip : TextView = findViewById(R.id.userZip) as TextView
        val adress : TextView = findViewById(R.id.userAdress) as TextView
        val city : TextView = findViewById(R.id.userCity) as TextView

        val inputFirstName = firstName.text.toString()
        val inputLastName = lastName.text.toString()
        val inputDateOfBirth = dateOfBirth.text.toString()
        val inputPhone = phoneNmb.text.toString()
        val inputZipCode = zip.text.toString()
        val inputAdress = adress.text.toString()
        val inputCity = city.text.toString()

        val user = hashMapOf(
            "First name" to inputFirstName,
            "Last name" to inputLastName,
            "Phone number" to inputPhone,
            "Date of birth" to inputDateOfBirth,
            "Zip Code" to inputZipCode,
            "Adress" to inputAdress,
            "City" to inputCity,
            "Notiser" to true
        )
        val currentUser = Firebase.auth.currentUser
        val userid = currentUser?.uid

        db.collection("users") //Koppla userId
            .document("$userid")
            .set(user)
            .addOnSuccessListener {
                finish()
                val intent = Intent(this,UserProfileActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener {
            }
        }

}



























