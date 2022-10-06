package com.example.groceryapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ShoppingListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)


        val searchBtn = findViewById(R.id.btn_search) as ImageButton
        val shoppingListBtn = findViewById(R.id.btn_shoppingList) as ImageButton
        val favoritesBtn = findViewById(R.id.btn_favorites) as ImageButton
        val homeBtn = findViewById(R.id.btn_home) as ImageButton
        val settingsBtn = findViewById(R.id.btn_settings) as ImageButton

        shoppingListBtn.setBackgroundColor(getResources().getColor(R.color.white))

        homeBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(com.google.android.material.R.anim.abc_tooltip_enter, androidx.appcompat.R.anim.abc_tooltip_exit)
            finish()
        }

        favoritesBtn.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
            overridePendingTransition(com.google.android.material.R.anim.abc_tooltip_enter, androidx.appcompat.R.anim.abc_tooltip_exit)
            finish()

        }

        searchBtn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            overridePendingTransition(com.google.android.material.R.anim.abc_tooltip_enter, androidx.appcompat.R.anim.abc_tooltip_exit)
            finish()
        }

        shoppingListBtn.setOnClickListener {
            val intent = Intent(this, ShoppingListActivity::class.java)
            startActivity(intent)
            overridePendingTransition(com.google.android.material.R.anim.abc_tooltip_enter, androidx.appcompat.R.anim.abc_tooltip_exit)
            finish()
        }

        settingsBtn.setOnClickListener {
            val intent = Intent(this, settingsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(com.google.android.material.R.anim.abc_tooltip_enter, androidx.appcompat.R.anim.abc_tooltip_exit)
            finish()
        }


        val db = Firebase.firestore
        val userid = Firebase.auth.currentUser?.uid

        var arrayAdapter: ArrayAdapter<*>
        val mListView = findViewById<ListView>(R.id.lv_Ica)

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
                    val store = document.getString("Storename") ?: "default"
                    var chainName = ""
                    for (chain in chainList){
                        if ("$store".contains(chain)){
                            chainName = chain
                        }
                    }
                    Log.i(TAG, "Chain: $chainName, Store: $store, product: $product")

                    intent.putExtra("product", "$product")
                    intent.putExtra("chain", "$chainName")
                    intent.putExtra("store", "$store")
                    intent.putExtra("activity", "ShoppingListActivity")
                    startActivity(intent)
                }
        }
    }
}