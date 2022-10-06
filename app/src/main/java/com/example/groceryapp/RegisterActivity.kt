package com.example.groceryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import java.util.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //Initialize Firebase Auth
        auth = Firebase.auth

        val loginText: TextView = findViewById(R.id.LogInHere_now)
        loginText.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val registerButton: Button = findViewById(R.id.button)

        registerButton.setOnClickListener{
            performSignUp()
        }
    }

    private fun performSignUp(){
        val db = Firebase.firestore

        val email = findViewById<EditText>(R.id.editTextTextEmailAddress3)
        val password = findViewById<EditText>(R.id.editTextTextPassword2)
        val firstName = findViewById<EditText>(R.id.editTextTextEmailAddress4)
        val lastName = findViewById<EditText>(R.id.editTextTextEmailAddress5)
        val phoneNum = findViewById<EditText>(R.id.editTextPhone)
        val dateOfBirth = findViewById<EditText>(R.id.textView)
        val zipCode = findViewById<EditText>(R.id.editTextTextPostalAddress)
        val adressField = findViewById<EditText>(R.id.adress)
        val city = findViewById<EditText>(R.id.city)


        val inputEmail = email.text.toString()
        val inputPassword = password.text.toString()
        val inputFirstName = firstName.text.toString()
        val inputLastName = lastName.text.toString()
        val inputPhone = phoneNum.text.toString()
        val inputDateOfBirth = dateOfBirth.text.toString()
        val inputZipCode = zipCode.text.toString()
        val inputAdress = adressField.text.toString()
        val inputCity = city.text.toString()
        

        if(email.text.isEmpty() || password.text.isEmpty()){

            Toast.makeText(this,"Please fill all fields", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val user = hashMapOf(
            "First name" to inputFirstName,
            "Last name" to inputLastName,
            "Phone number" to inputPhone,
            "Date of birth" to inputDateOfBirth,
            "Zip Code" to inputZipCode,
            "Adress" to inputAdress,
            "City" to inputCity,
        )

        auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val currentUser = Firebase.auth.currentUser
                    val userid = currentUser?.uid



                    db.collection("users") //Koppla userId
                        .document("$userid")
                        .set(user)
                        .addOnSuccessListener {
                        }
                        .addOnFailureListener {
                        }

                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this,HomeActivity::class.java)
                    startActivity(intent)

                    Toast.makeText(baseContext, "Success",
                        Toast.LENGTH_SHORT)
                        .show()

                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            }

            .addOnFailureListener(){
                Toast.makeText(this, "Error occured ${it.localizedMessage}", Toast.LENGTH_SHORT)
                    .show()
            }
    }
}