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
            .addOnFailureListener { }

        mListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val product = parent.getItemAtPosition(position)
            val intent = Intent(this, PopOutActivity::class.java)
            var chain = ""

            db.collection("users").document("$uid")
            .collection("History").get()
            .addOnSuccessListener { documents ->
                for(document in documents)
                {
                    val store = document.getString("Store") ?: "Default"
                    val storeChain = document.getString("Storechain") ?: "Default"

                    if(storeChain == "ICA")
                    {
                        chain = "Ica"       //Behövs pga structuren i collection Erbjudanden_sok: "Ica: produktnamn"
                    }
                    else {
                        chain = storeChain
                    }

                    val storeProduct = chain+": "+product

                    intent.putExtra("product", storeProduct) //skickar med ex: "Coop: productname" vs "productname"
                    intent.putExtra("store", store)
                    intent.putExtra("chain", chain)

                    startActivity(intent)

                }
            }
        }
    }
}