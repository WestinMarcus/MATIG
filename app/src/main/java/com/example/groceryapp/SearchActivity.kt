package com.example.groceryapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
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

        val searchBtn = findViewById(R.id.btn_search) as ImageButton
        val shoppingListBtn = findViewById(R.id.btn_shoppingList) as ImageButton
        val favoritesBtn = findViewById(R.id.btn_favorites) as ImageButton
        val homeBtn = findViewById(R.id.btn_home) as ImageButton
        val settingsBtn = findViewById(R.id.btn_settings) as ImageButton

        searchBtn.setBackgroundColor(getResources().getColor(R.color.button_row_highlight))

        //För att fylla onCreate med stores
        setStores()

        homeBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        favoritesBtn.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
            finish()
        }
        searchBtn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }
        shoppingListBtn.setOnClickListener {
            val intent = Intent(this, ShoppingListActivity::class.java)
            startActivity(intent)
            finish()
        }
        settingsBtn.setOnClickListener {
            val intent = Intent(this, settingsActivity::class.java)
            startActivity(intent)
            finish()
        }

        val button_toggle = findViewById(R.id.btn_toggle) as ToggleButton
        var searchToggle = ""
        button_toggle.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked){
                searchToggle = "Products"
                setProducts()
            }
            else{
                searchToggle = "Stores"
                setStores()

            }
        }
    }

    private fun setStores(){
        val db = Firebase.firestore
        val storeChainList = mutableListOf<String>()
        val storeList = mutableListOf<String>()
        val storeAdressList = mutableListOf<String>()
        val mListView = findViewById<ListView>(R.id.lv_search)
        val search = findViewById<SearchView>(R.id.searchview)

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
                            }
                            val customAdapter = CustomAdapter(this, storeList)
                            mListView.adapter = customAdapter
                        }.addOnFailureListener { exception -> Log.w(TAG, "Error getting documents.", exception) }
                }
            }

        val customAdapter = CustomAdapter(this, storeList)
        mListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedStore = parent.getItemAtPosition(position)
            val intent = Intent(this, ItemListActivity::class.java)
            intent.putExtra("store", "$selectedStore")
            startActivity(intent)
        }
        search.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                search.clearFocus()
                if(storeList.contains(p0)){
                    customAdapter.filter.filter(p0)
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                customAdapter.filter.filter(p0)
                return false
            }
        })
    }

    private fun setProducts(){
        var shoppingListAdapter: ShoppingListAdapter
        val searchProductList = mutableListOf<String>()
        val searchPriceList = mutableListOf<String>()
        val storeList = mutableListOf<String>()
        val mListView = findViewById<ListView>(R.id.lv_search)

        shoppingListAdapter = ShoppingListAdapter(this, searchProductList, searchPriceList)
        mListView.adapter = shoppingListAdapter

        val db = Firebase.firestore
        val search = findViewById<SearchView>(R.id.searchview)

        db.collection("Erbjudanden_sok")
            .get()
            .addOnSuccessListener { products ->
                for (product in products) {
                    searchProductList.add(product.id)
                    val price = product.getString("Pris") ?: "default"
                    searchPriceList.add(price)

                    shoppingListAdapter = ShoppingListAdapter(this, searchProductList, searchPriceList)
                    mListView.adapter = shoppingListAdapter
                }

            }
        mListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val product = parent.getItemAtPosition(position)
            val intent = Intent(this, PopOutActivity::class.java)
            intent.putExtra("product", "$product")
            Log.w(TAG, "clicked in search: product: $product ")
            startActivity(intent)
        }
        search.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                search.clearFocus()
                if(storeList.contains(p0)){
                    shoppingListAdapter.filter.filter(p0)
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                shoppingListAdapter.filter.filter(p0)
                return false
            }
        })

    }

}