package com.example.groceryapp

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
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

        /*       vid "favorit"-knapptryck -> skicka till db.user.favorites(storename)        */


    }
}