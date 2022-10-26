package com.example.groceryapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

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
        val tempList = mutableListOf<String>()
        val storeAdressList = mutableListOf<String>()
        val search = findViewById<SearchView>(R.id.searchview)

        val newRecyclerView : RecyclerView = findViewById(R.id.rv_food_list)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)
        val adapter = StoreAdapter(tempList)

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
                                tempList.add(document.id)
                                storeAdressList.add(document.getString("Adress") ?: "default")
                            }
                            newRecyclerView.adapter = adapter
                        }.addOnFailureListener { exception -> Log.w(TAG, "Error getting documents.", exception) }
                }

            }


        adapter.setOnItemClickListener(object : StoreAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

                val intent = Intent(this@SearchActivity, ItemListActivity::class.java)
                intent.putExtra("store", tempList[position])
                startActivity(intent)

            }
        })

        search.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                tempList.clear()
                val searchText = p0!!.toLowerCase(Locale.getDefault())
                if(searchText.isNotEmpty()){

                    storeList.forEach{

                        if (it.toLowerCase(Locale.getDefault()).contains(searchText)){
                            tempList.add(it)
                        }

                    }

                    newRecyclerView.adapter!!.notifyDataSetChanged()

                } else {

                    tempList.clear()
                    tempList.addAll(storeList)
                    newRecyclerView.adapter!!.notifyDataSetChanged()

                }
                return false
            }
        })
    }

    private fun setProducts(){
        val searchProductList = mutableListOf<String>()
        val foodItemList = mutableListOf<FoodItem>()
        val tempList = mutableListOf<FoodItem>()

        val newRecyclerView : RecyclerView = findViewById(R.id.rv_food_list)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        val adapter = FoodItemAdapter(tempList)

        val db = Firebase.firestore
        val search = findViewById<SearchView>(R.id.searchview)



        db.collection("Erbjudanden_sok")
            .get()
            .addOnSuccessListener { products ->
                for (product in products) {
                    searchProductList.add(product.id)
                    var price = product.getString("Pris") ?: ""
                    price = removePriceSign(price)+"kr"
                    val newItem = FoodItem(product.id, price)
                    foodItemList.add(newItem)
                }
                tempList.addAll(foodItemList)

                newRecyclerView.adapter = adapter

            }

        adapter.setOnItemClickListener(object : FoodItemAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {

                val intent = Intent(this@SearchActivity, PopOutActivity::class.java)
                intent.putExtra("product", tempList[position].item)
                startActivity(intent)



            }

        })

        search.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                tempList.clear()
                val searchText = p0!!.toLowerCase(Locale.getDefault())
                if(searchText.isNotEmpty()){

                    foodItemList.forEach{

                        if (it.item.toLowerCase(Locale.getDefault()).contains(searchText)){
                            tempList.add(it)
                        }

                    }

                    newRecyclerView.adapter!!.notifyDataSetChanged()

                } else {

                    tempList.clear()
                    tempList.addAll(foodItemList)
                    newRecyclerView.adapter!!.notifyDataSetChanged()

                }
                return false
            }
        })

    }
    private fun removePriceSign(price: String): String
    {
        var fixedPrice = ""
        if (price.contains(":-"))
        {
            fixedPrice = price.dropLast(2)
        }
        else if (price.contains("%"))
        {
            fixedPrice = price.dropLast(1)
        }
        else
        {
            fixedPrice = price
        }
        return fixedPrice
    }

}