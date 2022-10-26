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
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HistoryPopOutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_pop_out)
        val product = intent.getStringExtra("product")
        val chain = intent.getStringExtra("chain")
        var store = intent.getStringExtra("store")

        Log.i(TAG, "HistoryPopOut: Chain: $chain, Store: $store, Product: $product")

        val productHeader: TextView = findViewById(R.id.tv_productName)
        productHeader.text = "$product"
        val removeFromHistory = findViewById<Button>(R.id.btn_removeFromHistory)
        val db = Firebase.firestore
        val userid = Firebase.auth.currentUser?.uid

        val closeBtn: ImageButton = findViewById(R.id.btn_close)
        closeBtn.setOnClickListener {
            finish()
        }

        val price: TextView = findViewById(R.id.tv_productPrice)
        val info: TextView = findViewById(R.id.tv_productInfo)
        val priceRelative: TextView = findViewById(R.id.tv_productPriceRelative)

        db.collection("Erbjudanden_sok")
        .document("$product")
        .get()
        .addOnSuccessListener { document ->
            var productInfo = ""
            val productMap = document.data
            productMap?.put("Storename", "$store")

            if(chain != "Lidl")
            {
                productInfo = document.getString("Övrig information") ?: ""
            }
            val productPrice = document.getString("Pris") ?: ""
            val productPriceWeight = document.getString("Jämfört pris(kg)") ?: ""
            val productPriceVol = document.getString("Jämfört pris(lit)") ?: ""

            price.text = "Pris: $productPrice"
            info.text = productInfo
            val inputText: String
            if (productPriceWeight != "Data saknas") {
                inputText = productPriceWeight + "kr/kg"
                priceRelative.text = inputText
            }else if (productPriceVol != "Data saknas") {
                inputText = productPriceVol + "kr/l"
                priceRelative.text = inputText
            }
        }

        /*---------------remove product from history list--------------------*/
        removeFromHistory.setOnClickListener {
            Log.i(ContentValues.TAG, "removed product from history list: $product")

            db.collection("users").document("$userid")
            .collection("History").document("$product")
            .delete()
            if (intent.getStringExtra("activity") == "PurchaseHistoryActivity")
            {
                val intent = Intent(this, PurchaseHistoryActivity::class.java)
                startActivity(intent)
            }
            finish()
        }
    }
}