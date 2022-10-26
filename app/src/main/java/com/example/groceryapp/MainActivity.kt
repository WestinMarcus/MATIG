package com.example.groceryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        val createUsrBtn = findViewById(R.id.textView2) as TextView

        createUsrBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val loginButton: Button = findViewById(R.id.LoginBtn)

        loginButton.setOnClickListener {
            performLogin()
            //val intent = Intent(this, HomeActivity::class.java)
            //startActivity(intent)
        }

        val forgotPasswordButton = findViewById(R.id.forgorPass) as TextView

        forgotPasswordButton.setOnClickListener {
            val intent = Intent(this, passwordResetActivity::class.java)
            startActivity(intent)
        }
    }

        private fun performLogin(){
            val email: EditText = findViewById(R.id.editTextTextEmailAddress)
            val password: EditText = findViewById(R.id.editTextTextEmailAddress2)

            if(email.text.isEmpty() || password.text.isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return
            }

            val emailInput = email.text.toString()
            val passwordInput = password.text.toString()

            auth.signInWithEmailAndPassword(emailInput, passwordInput)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, navigate to main_view
                        val intent = Intent(this,HomeActivity::class.java)
                        startActivity(intent)
                        finish()

                        Toast.makeText(baseContext, "Success", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }

                .addOnFailureListener{
                    Toast.makeText(baseContext, "Authentication failed. ${it.localizedMessage}",
                        Toast.LENGTH_SHORT).show()
                }
        }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // Signed in
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
        } else {
        }
    }

}