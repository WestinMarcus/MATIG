package com.example.groceryapp

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout.TabGravity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ItemListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)
        val store = intent.getStringExtra("store")
        val storeHeader: TextView = findViewById(R.id.tvStoreItemList)
        storeHeader.text = "$store"
        val chainList = listOf("Coop", "ICA", "Ica", "Willys", "Lidl")

        //Getting chain name for fetching products
        var chainName = ""
        for (chain in chainList){
            if ("$store".contains(chain)){
                chainName = chain
            }
        }

        val db = Firebase.firestore
        val foodItemList = mutableListOf<String>()
        var arrayAdapter: ArrayAdapter<*>
        val foodListView = findViewById<ListView>(R.id.lvFoodItems)
        val addFavBtn = findViewById<Button>(R.id.btn_addFavorite)
        val removeFavBtn = findViewById<Button>(R.id.btn_removeFavorite)

        db.collection("Aktiva erbj.").document("$chainName").collection("$store")
            .get()
            .addOnSuccessListener { result ->
                for (document in result)
                {
                    foodItemList.add(document.id)
                }
                arrayAdapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, foodItemList)
                foodListView.adapter = arrayAdapter
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
        //pop out dialog för vald produkt
        foodListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val product = parent.getItemAtPosition(position)
            val intent = Intent(this, PopOutActivity::class.java)
            intent.putExtra("product", "$product")
            intent.putExtra("chain", "$chainName")
            intent.putExtra("store", "$store")
            startActivity(intent)
        }

        addFavBtn.setOnClickListener {
            val userid = Firebase.auth.currentUser?.uid
            val data = hashMapOf(
                "name" to "placeholder"
            )
            db.collection("users")
                .document("$userid")
                .collection("Favorites")
                .document("$store")
                .set(data)
                .addOnSuccessListener { Log.i(TAG, "added fav store as document: $store") }
                .addOnFailureListener { Log.i(TAG, "failed to add fav store as document") }
        }

        removeFavBtn.setOnClickListener {
            val userid = Firebase.auth.currentUser?.uid

            db.collection("users")
                .document("$userid")
                .collection("Favorites")
                .document("$store")
                .delete()
                .addOnSuccessListener {
                    Log.i(TAG, "removed fav store: $store")
                    if (intent.getStringExtra("activity") == "FavoritesActivity")
                    {
                        val intent = Intent(this, FavoritesActivity::class.java)
                        startActivity(intent)
                    }
                    finish()
                }
                .addOnFailureListener { Log.i(TAG, "failed to remove fav store as document") }
        }
    }
}