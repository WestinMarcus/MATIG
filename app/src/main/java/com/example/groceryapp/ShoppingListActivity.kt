package com.example.groceryapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
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

        shoppingListBtn.setBackgroundColor(getResources().getColor(R.color.button_row_highlight))

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

        val db = Firebase.firestore
        val userid = Firebase.auth.currentUser?.uid
        val addProductBtn = findViewById<Button>(R.id.btn_addProduct)

        val productListView = findViewById<ListView>(R.id.lv_Ica)

        val itemList = mutableListOf<String>()
        val priceList = mutableListOf<String>()

        val chainList = listOf("Coop", "ICA", "Ica", "Willys", "Lidl", "Note")

        addProductBtn.setOnClickListener{
            val intent = Intent(this, addProductPopOutActivity::class.java)
            startActivity(intent)
        }

        db.collection("users").document("$userid")
        .collection("Shoppinglist")
        .get()
        .addOnSuccessListener { products ->
            for (product in products)
            {
                itemList.add(product.id)
                var price = product.getString("Pris") ?: ""
                price = removePriceSign(price)
                if(price != ""){ price += "kr" }
                priceList.add(price)
            }

            val shoppingListAdapter = ShoppingListAdapter(this, itemList, priceList)
            productListView.adapter = shoppingListAdapter
        }

        productListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val product = parent.getItemAtPosition(position)
            val intent = Intent(this, ShoppingPopOutActivity::class.java)

            db.collection("users").document("$userid")
            .collection("Shoppinglist").document("$product")
            .get()
            .addOnSuccessListener { document ->
                val store = document.getString("Storename") ?: "default"
                var chainName = ""
                for (chain in chainList){ if (store.contains(chain)){ chainName = chain } }
                intent.putExtra("product", "$product")
                intent.putExtra("chain", chainName)
                intent.putExtra("store", store)
                intent.putExtra("activity", "ShoppingListActivity")

                startActivity(intent)
            }
        }



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