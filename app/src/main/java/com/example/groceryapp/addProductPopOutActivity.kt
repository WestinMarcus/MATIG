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

class addProductPopOutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product_pop_out)

        Log.i(TAG, "addProductPopOut")

        val closeBtn: ImageButton = findViewById(R.id.btn_close)
        closeBtn.setOnClickListener {
            finish()
        }

        val addProductBtn = findViewById<Button>(R.id.btn_addProduct)
        val db = Firebase.firestore
        val userid = Firebase.auth.currentUser?.uid

        addProductBtn.setOnClickListener {
            val userProduct: TextView = findViewById(R.id.editText)
            var productName = userProduct.text.toString()

            Log.i(TAG, "input: $productName")

            val data = hashMapOf(
                "Product" to productName,
                "Storename" to "Note",
                "Pris" to "",
                "Övrig information" to "Personal note",
                "Jämfört pris(kg)" to "",
                "Jämfört pris(lit)" to ""
            )
            productName = ">Note: $productName"
            db.collection("users").document("$userid")
            .collection("Shoppinglist").document(productName)
            .set(data)
            .addOnSuccessListener {
                Log.i(TAG, "added new product: $productName")
                finish()
            }
            val intent = Intent(this, ShoppingListActivity::class.java)
            startActivity(intent)

        }
    }
}