package com.example.groceryapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val button_toggle = findViewById(R.id.btn_toggle) as ToggleButton
        var searchToggle = ""
        button_toggle.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked)
                searchToggle = "Products"
            else
                searchToggle = "Stores"
            Toast.makeText(this, searchToggle, Toast.LENGTH_SHORT).show()
        }
        //-------------------------------------------------
        //adds new users collection with new document containing a user
        val db = Firebase.firestore
        val storeChainList = mutableListOf<String>()
        val storeList = mutableListOf<String>()
        val storeAdressList = mutableListOf<String>()

        var arrayAdapter: ArrayAdapter<*>
        val mListView = findViewById<ListView>(R.id.lv_search)

        db.collection("Store chains")
            .get()
            .addOnSuccessListener { chainList ->
                for (document in chainList) {
                    storeChainList.add(document.id)
                }
                for(chainName in storeChainList)
                {
                    db.collection("store list").document("Butik info").collection("$chainName")
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result)
                            {
                                storeList.add(document.id)

                                storeAdressList.add(document.getString("Adress") ?: "default")

                                Log.d(TAG, "${document.id} => ${document.data}")
                            }
                            arrayAdapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, storeList)
                            mListView.adapter = arrayAdapter
                        }
                        .addOnFailureListener { exception ->
                            Log.w(TAG, "Error getting documents.", exception)
                        }
                }
            }
            .addOnFailureListener { }

        mListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedStore = parent.getItemAtPosition(position)
            val intent = Intent(this, ItemListActivity::class.java)
            intent.putExtra("store", "$selectedStore")

            startActivity(intent)
        }
    }
}