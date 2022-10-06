package com.example.groceryapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PopOutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_out)
        val product = intent.getStringExtra("product")
        val chain = intent.getStringExtra("chain")
        val store = intent.getStringExtra("store")
        val productHeader: TextView = findViewById(R.id.tv_productName)
        productHeader.text = "$product"
        val db = Firebase.firestore
        // tv_productName, tv_productInfo, tv_productPriceEach, tv_productPriceRelative
        // btn_addToShoppingList, btn_closePopOut
        //var productInfo = ""
        //var productPrice = ""
        //var productPriceWeight = ""
        //var productPriceVol = ""
        //var productPriceEach = ""
        val price: TextView = findViewById(R.id.tv_productPrice)
        val info: TextView = findViewById(R.id.tv_productInfo)
        val priceRelative: TextView = findViewById(R.id.tv_productPriceRelative)

        db.collection("Aktiva erbj.").document("$chain").collection("$store").document("$product")
            .get()
            .addOnSuccessListener { document ->
                val productInfo = document.getString("Övrig information") ?: "default"
                val productPrice = document.getString("Pris") ?: "default"
                val productPriceWeight = document.getString("Jämfört pris(kg)") ?: "default"
                val productPriceVol = document.getString("Jämfört Pris(lit)")?:"default"
                val productPriceEach = document.getString("Jämfört Pris(st)") ?: "default"
                //price.setText(productPrice).toString()

                price.text = productPrice
                info.text = productInfo
                priceRelative.text = productPriceWeight
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }



    }
}