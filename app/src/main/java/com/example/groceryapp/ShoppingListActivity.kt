package com.example.groceryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ShoppingListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        val db = Firebase.firestore
        val userid = Firebase.auth.currentUser?.uid

        var arrayAdapter: ArrayAdapter<*>
        val mListView = findViewById<ListView>(R.id.lv_shoppingList)

        val productList = mutableListOf<String>()
        val chainList = listOf("Coop", "ICA", "Ica", "Willys", "Lidl")

        db.collection("users").document("$userid").collection("Shoppinglist")
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

            db.collection("users")
                .document("$userid")
                .collection("Shoppinglist")
                .document("$product")
                .get()
                .addOnSuccessListener { document ->
                    val store = document.getString("store") ?: "default"
                    var chainName = ""
                    for (chain in chainList){
                        if ("$store".contains(chain)){
                            chainName = chain
                        }
                    }
                    intent.putExtra("product", "$product")
                    intent.putExtra("chain", "$chainName")
                    intent.putExtra("store", "$store")
                    startActivity(intent)
                }


        }
    }
}