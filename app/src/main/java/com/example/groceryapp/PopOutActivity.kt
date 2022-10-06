package com.example.groceryapp

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
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

        Log.i(TAG, "PopOut: Chain: $chain, Store: $store, Product: $product")

        val productHeader: TextView = findViewById(R.id.tv_productName)
        productHeader.text = "$product"
        val addToShoppingList = findViewById<Button>(R.id.btn_addToShoppingList)
        val removeFromShoppingList = findViewById<Button>(R.id.btn_removeFromShoppingList)
        val db = Firebase.firestore
        val userid = Firebase.auth.currentUser?.uid

        val price: TextView = findViewById(R.id.tv_productPrice)
        val info: TextView = findViewById(R.id.tv_productInfo)
        val priceRelative: TextView = findViewById(R.id.tv_productPriceRelative)

        db.collection("Aktiva erbj.").document("$chain").collection("$store").document("$product")
            .get()
            .addOnSuccessListener { document ->
                val productInfo = document.getString("Övrig information") ?: "default"
                val productPrice = document.getString("Pris") ?: "default"
                val productPriceWeight = document.getString("Jämfört pris(kg)") ?: "default"
                val productPriceVol = document.getString("Jämfört pris(lit)")?:"default"

                val productMap = document.data
                productMap?.put("Storename", "$store")

                price.text = productPrice
                info.text = productInfo
                val inputText: String
                if (productPriceWeight != "Data saknas") {
                    inputText = productPriceWeight + "kr/kg"
                    priceRelative.text = inputText
                }else if (productPriceVol != "Data saknas") {
                    inputText = productPriceVol + "l/kg"
                    priceRelative.text = inputText
                }
                /*---------------add product to shopping list--------------------*/
                addToShoppingList.setOnClickListener {
                    if (productMap != null) {
                        db.collection("users")
                            .document("$userid")
                            .collection("Shoppinglist")
                            .document("$product")
                            .set(productMap)
                            .addOnSuccessListener {
                                Log.i(ContentValues.TAG, "added product to shopping list: $product")
                                finish()
                            }
                            .addOnFailureListener { Log.i(ContentValues.TAG, "failed to add product to shopping list") }
                    }
                }
                /*---------------remove product from shopping list--------------------*/

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
        removeFromShoppingList.setOnClickListener {
            db.collection("users")
                .document("$userid")
                .collection("Shoppinglist")
                .document("$product")
                .delete()
                .addOnSuccessListener {
                    Log.i(ContentValues.TAG, "removed product from shopping list: $product")
                    if (intent.getStringExtra("activity") == "ShoppingListActivity")
                    {
                        val intent = Intent(this, ShoppingListActivity::class.java)
                        startActivity(intent)
                    }
                    finish()
                }
                .addOnFailureListener { Log.i(ContentValues.TAG, "failed to delete product from shopping list") }

        }
    }
}