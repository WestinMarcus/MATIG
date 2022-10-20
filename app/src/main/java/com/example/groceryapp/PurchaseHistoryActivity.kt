package com.example.groceryapp

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import com.google.android.material.tabs.TabLayout.TabGravity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PurchaseHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_history)

        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        val uid = user?.uid

        var arrayAdapter: ArrayAdapter<*>
        val mListView = findViewById<ListView>(R.id.lv_purchase_history)

        val productList = mutableListOf<String>()

        db.collection("users").document("$uid").collection("History")
        .get()
        .addOnSuccessListener { products ->
            for (product in products)
            {
                productList.add(product.id)
            }
            arrayAdapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, productList)
            mListView.adapter = arrayAdapter
        }
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, productList)
        mListView.adapter = arrayAdapter

        mListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val product = parent.getItemAtPosition(position)
            val intent = Intent(this, HistoryPopOutActivity::class.java)

            db.collection("users").document("$uid")
            .collection("History").document("$product")
            .get()
            .addOnSuccessListener { document ->

                val store = document.getString("Store") ?: "Default"
                val chain = document.getString("Storechain") ?: "Default"

                intent.putExtra("product", "${document.id}") //skickar med ex: "Coop: productname" vs "productname"
                intent.putExtra("store", store)
                intent.putExtra("chain", chain)
                intent.putExtra("activity", "PurchaseHistoryActivity")

                startActivity(intent)
            }
        }
    }
}