package com.example.groceryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class passwordResetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)



        //Gå tillbaka funktion
        val backBtn: ImageView = findViewById(R.id.backButton1) as ImageView
        backBtn.setOnClickListener {
            finish()
        }

        val userMail: TextView = findViewById(R.id.passReset)

        val submitBtn: Button = findViewById(R.id.btn_submit)
        submitBtn.setOnClickListener {
            // Skicka emailformulär för att återställa lösenord
            val email= userMail.text.toString()
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

    }
}