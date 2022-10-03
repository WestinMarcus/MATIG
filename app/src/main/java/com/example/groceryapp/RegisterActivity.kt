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

        // Initialize Firebase Auth
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

        var email = findViewById<EditText>(R.id.editTextTextEmailAddress3)
        var password = findViewById<EditText>(R.id.editTextTextPassword2)
        var firstName = findViewById<EditText>(R.id.editTextTextEmailAddress4)
        var lastName = findViewById<EditText>(R.id.editTextTextEmailAddress5)
        var phoneNum = findViewById<EditText>(R.id.editTextPhone)
        var dateOfBirth = findViewById<DatePicker>(R.id.datePicker)
        var zipCode = findViewById<EditText>(R.id.editTextTextPostalAddress)
        var adressField = findViewById<EditText>(R.id.adress)
        var city = findViewById<EditText>(R.id.city)


        var inputEmail = email.text.toString()
        var inputPassword = password.text.toString()
        var inputFirstName = firstName.text.toString()
        var inputLastName = lastName.text.toString()
        var inputPhone = phoneNum.text.toString()
        var inputDateOfBirth: String? = ""

        val today = Calendar.getInstance()
        dateOfBirth.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
        { view, year, month, day ->
            val month = month + 1
            inputDateOfBirth = ": $day/$month/$year"
        }

        var inputZipCode = zipCode.text.toString()
        var inputAdress = adressField.text.toString()
        var inputCity = city.text.toString()
        

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