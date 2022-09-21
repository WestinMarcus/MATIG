package com.example.groceryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ItemListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)
        val store = intent.getStringExtra("store");
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
        val chainHeader: TextView = findViewById(R.id.textView7)
        chainHeader.text = chainName
    }
}