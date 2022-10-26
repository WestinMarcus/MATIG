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

class PopOutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_out)
        val product = intent.getStringExtra("product")
        val chain = intent.getStringExtra("chain")
        var store = intent.getStringExtra("store")

        Log.i(TAG, "Itemlist PopOut: Chain: $chain, Store: $store, Product: $product")

        val productHeader: TextView = findViewById(R.id.tv_productName)
        productHeader.text = "$product"
        val addToShoppingList = findViewById<Button>(R.id.btn_addToShoppingList)
        val db = Firebase.firestore
        val userid = Firebase.auth.currentUser?.uid

        val closeBtn: ImageButton = findViewById(R.id.btn_close)
        closeBtn.setOnClickListener {
            finish()
        }
        val price: TextView = findViewById(R.id.tv_productPrice)
        val info: TextView = findViewById(R.id.tv_productInfo)
        val priceRelative: TextView = findViewById(R.id.tv_productPriceRelative)

        db.collection("Erbjudanden_sok")
        .document("$product")
        .get()
        .addOnSuccessListener { document ->

            store = getStoreFromProductName(product)

            var productInfo = ""

            val productMap = document.data
            productMap?.put("Storename", store)

            Log.i(TAG, "produktNamn = ${product}")
            if(chain != "Lidl")
            {
                productInfo = document.getString("Övrig information") ?: ""
            }
            var productPrice = document.getString("Pris") ?: ""
            productPrice = removePriceSign(productPrice)
            var productPriceWeight = document.getString("Jämfört pris(kg)") ?: ""
            var productPriceVol = document.getString("Jämfört pris(lit)") ?: ""

            price.text = "Pris: ${productPrice}kr"
            info.text = productInfo
            val inputText: String
            if (productPriceWeight != "Information saknas" && productPriceWeight != "information saknas") {
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
            /*---------------add product to shopping list--------------------*/
            addToShoppingList.setOnClickListener {
                Log.i(ContentValues.TAG, "adding product to shopping list: $product")

                if (productMap != null) {
                    db.collection("users").document("$userid")
                    .collection("Shoppinglist").document("$product")
                    .set(productMap)
                    .addOnSuccessListener {
                        Log.i(ContentValues.TAG, "added product successfully")
                        finish()
                    }
                    .addOnFailureListener{ Log.i(ContentValues.TAG, "failure to add product to shopping list: $product")}
                }
            }
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
        Log.i(TAG, "fixDecimals: value: $value")

        val floatValue = value.toFloat()
        val fixedValue = String.format("%.2f", floatValue)

        Log.i(TAG, "formated value: $fixedValue")
        return fixedValue
    }
    private fun removePriceSign(price: String): String
    {
        Log.i(TAG, "popout price: $price")

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
        Log.i(TAG, "popout fixedPrice: $price")
        return fixedPrice
    }
}