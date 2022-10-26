package com.example.groceryapp

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.android.material.tabs.TabLayout.TabGravity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class ItemListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)
        val store = intent.getStringExtra("store")
        val storeHeader: TextView = findViewById(R.id.tvStoreItemList)
        storeHeader.text = "$store"
        val chainList = listOf("Coop", "ICA", "Ica", "Willys", "Lidl")

        val backBtn = findViewById<ImageView>(R.id.image_back_settings)
        backBtn.setOnClickListener {
            finish()
        }
        //Getting chain name for fetching products
        var chainName = ""
        for (chain in chainList){
            if ("$store".contains(chain)){
                 chainName = chain
            }
        }
        Log.i(TAG, "In itemlist: $chainName, $store")
        val userid = Firebase.auth.currentUser?.uid
        val db = Firebase.firestore
        val foodItemList = mutableListOf<FoodItem>()
        val tempList = mutableListOf<FoodItem>()

        val newRecyclerView : RecyclerView = findViewById(R.id.rv_food_list)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)
        val adapter = FoodItemAdapter(tempList)

        val addFavBtn = findViewById<ImageButton>(R.id.btn_addFavorite)
        val search = findViewById<SearchView>(R.id.sv_itemList)

        db.collection("Aktiva erbj.").document("$chainName").collection("$store")
        .get()
        .addOnSuccessListener { result ->
            for (document in result)
            {
                var price = document.getString("Pris") ?: ""
                price = removePriceSign(price)+"kr"
                val newItem = FoodItem(document.id, price)
                foodItemList.add(newItem)
            }
            tempList.addAll(foodItemList)

            newRecyclerView.adapter = adapter
        }

        //pop out dialog för vald produkt
        adapter.setOnItemClickListener(object : FoodItemAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val product = tempList[position].item
                val intent = Intent(this@ItemListActivity, PopOutActivity::class.java)

                var productName = ""
                if (chainName == "ICA") {
                    productName = "Ica: ${product}"
                } else {
                    productName = "$chainName: ${product}"
                }
                intent.putExtra("product", "$productName")
                intent.putExtra("store", "$store")
                intent.putExtra("chain", "$chainName")

                startActivity(intent)
            }
        })



        db.collection("users").document("$userid").collection("Favorites")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents)
                {
                    if (document.id == store){
                        addFavBtn.setImageResource(R.drawable.ic_baseline_star)
                        addFavBtn.setOnClickListener {
                            db.collection("users")
                                .document("$userid")
                                .collection("Favorites")
                                .document("$store")
                                .delete()
                                .addOnSuccessListener {
                                    addFavBtn.setImageResource(R.drawable.ic_baseline_star_border)
                                    Log.i(TAG, "removed fav store: $store")
                                    if (intent.getStringExtra("activity") == "FavoritesActivity")
                                    {
                                        val intent = Intent(this, FavoritesActivity::class.java)
                                        startActivity(intent)
                                    }
                                    finish()
                                }
                        }
                    }
                }
            }


        addFavBtn.setOnClickListener {
            addFavBtn.setImageResource(R.drawable.ic_baseline_star)
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