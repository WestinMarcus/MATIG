package com.example.groceryapp

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class userAdressClass {


    fun test(): String {


        return test2()
    }

            fun test2() : String {
                val db = Firebase.firestore
                val user = Firebase.auth.currentUser
                val uid = user?.uid
                var userAdressen = ""

                db.collection("users").document("$uid")
                    .get()
                    .addOnSuccessListener { userDoc ->
                        val uCity = userDoc.getString("City") ?: "Default"
                        userAdressen = userDoc.getString("Adress") ?: "Default"
                        userAdressen += ", $uCity"

                    }

                do {
                }while (userAdressen == "")

                return  userAdressen
            }

}
