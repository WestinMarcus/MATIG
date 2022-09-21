package com.example.groceryapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore

class StoresActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stores)

        //-------------------------------------------------
        //adds new users collection with new document containing a user
        val db = Firebase.firestore
        val storeList = mutableListOf<String>()
        val storeAdressList = mutableListOf<String>()

        db.collection("store list").document("Butik info").collection("Coop")
            .get()
            // result gets all documents in collection
            .addOnSuccessListener { result ->
                for (document in result)
                {
                    storeList.add(document.id)
                    storeAdressList.add(document.getString("Adress") ?: "default")

                    Log.d(TAG, "${document.id} => ${document.data}")
                }
                // print storeList
                for((index, storeName) in storeList.withIndex())
                {
                    Log.i(TAG, "store at index: $index is $storeName")
                }

                for((index, storeAdress) in storeAdressList.withIndex())
                {
                    Log.i(TAG, "store at index: $index is $storeAdress")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

    }


}