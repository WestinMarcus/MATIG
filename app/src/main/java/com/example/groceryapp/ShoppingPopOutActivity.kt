package com.example.groceryapp

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ShoppingPopOutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_pop_out)
        val product = intent.getStringExtra("product")
        val chain = intent.getStringExtra("chain")
        var store = intent.getStringExtra("store")

        Log.i(TAG, "ShoppingPopOut: Chain: $chain, Store: $store, Product: $product")

        val productHeader: TextView = findViewById(R.id.tv_productName)
        productHeader.text = "$product"
        val purchaseBtn = findViewById<Button>(R.id.btn_purchaseProduct)
        val removeFromShoppingList = findViewById<Button>(R.id.btn_removeFromShoppingList)
        val db = Firebase.firestore
        val userid = Firebase.auth.currentUser?.uid

        val closeBtn: ImageButton = findViewById(R.id.btn_close)
        closeBtn.setOnClickListener {
            finish()
        }
        val price: TextView = findViewById(R.id.tv_productPrice)
        val info: TextView = findViewById(R.id.tv_productInfo)
        val priceRelative: TextView = findViewById(R.id.tv_productPriceRelative)

        db.collection("users").document("$userid")
        .collection("Shoppinglist").document("$product")
        .get()
        .addOnSuccessListener { document ->
            var productInfo = ""
            val productMap = document.data

            store = getStoreFromProductName(product)
            productMap?.put("Storename", store)

            if(chain != "Lidl")
            {
                productInfo = document.getString("Övrig information") ?: ""
            }
            var productPrice = document.getString("Pris") ?: ""
            var productPriceWeight = document.getString("Jämfört pris(kg)") ?: ""
            var productPriceVol = document.getString("Jämfört pris(lit)") ?: ""

            if(productPrice == "")
            {
                price.text = ""
            }else{
                productPrice = removePriceSign(productPrice)
                price.text = "Pris: ${productPrice}kr"
            }

            info.text = productInfo
            val inputText: String
            if (productPriceWeight == "") {
                priceRelative.text = productPriceWeight
            }else if (productPriceWeight != "Information saknas" && productPriceWeight != "information saknas") {
                productPriceWeight = removePriceSign(productPriceWeight)
                productPriceWeight = fixDecimals(productPriceWeight)
                inputText = productPriceWeight + "kr/kg"
                priceRelative.text = inputText
            }else if (productPriceVol != "Information saknas" && productPriceVol != "information saknas") {
                productPriceVol = removePriceSign(productPriceVol)
                productPriceVol = fixDecimals(productPriceVol)
                inputText = productPriceVol + "kr/l"
                priceRelative.text = inputText
            }
        }
        /*------------purchase product and remove from shopping list------------------*/
        purchaseBtn.setOnClickListener {
            Log.i(ContentValues.TAG, "purchase product: $product, removed from shopping list and added to history")
            val data = hashMapOf(
                "product" to "$product",
                "Store" to "$store",
                "Storechain" to "$chain"
            )
            db.collection("users").document("$userid")
                .collection("Shoppinglist").document("$product")
            db.collection("users").document("$userid")
                .collection("History").document("$product")
                .set(data)

            db.collection("users")
                .document("$userid")
                .collection("Shoppinglist")
                .document("$product")
                .delete()
                .addOnSuccessListener {
                    if (intent.getStringExtra("activity") == "ShoppingListActivity")
                    {
                        val intent = Intent(this, ShoppingListActivity::class.java)
                        startActivity(intent)
                    }
                    finish()
                }
                .addOnFailureListener { Log.i(ContentValues.TAG, "failed to purchase product in shopping list") }
        }

        /*------------------remove product from shopping list----------------------*/
        removeFromShoppingList.setOnClickListener {
            Log.i(ContentValues.TAG, "removed product from shopping list: $product")

            db.collection("users")
            .document("$userid")
            .collection("Shoppinglist")
            .document("$product")
            .delete()
            .addOnSuccessListener {
                if (intent.getStringExtra("activity") == "ShoppingListActivity")
                {
                    val intent = Intent(this, ShoppingListActivity::class.java)
                    startActivity(intent)
                }
                finish()
            }
            .addOnFailureListener { Log.i(ContentValues.TAG, "failed to delete product from shopping list") }
        }
    }

    private fun getStoreFromProductName(product: String?): String
    {
        var storeName = ""

        if (product != null) {
            if (product.contains("Ica")) {
                storeName = "ICA"
            }
            else if (product.contains("Coop")) {
                storeName = "Coop"
            }
            else if ( product.contains("Willys")) {
                storeName = "Willys"
            }
            else if (product.contains("Lidl")) {
                storeName = "Lidl"
            }
        }
        return storeName
    }
    private fun fixDecimals(value: String): String
    {
        val floatValue = value.toFloat()
        val fixedValue = String.format("%.2f", floatValue)

        return fixedValue
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