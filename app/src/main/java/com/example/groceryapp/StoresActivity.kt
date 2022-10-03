package com.example.groceryapp

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.os.IResultReceiver.Default
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import java.util.*

class StoresActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stores)

        val db = Firebase.firestore
        val storeChainList = mutableListOf<String>()
        val storeList = mutableListOf<String>()
        val storeAdressList = mutableListOf<String>()

        var arrayAdapter: ArrayAdapter<*>
        val mListView = findViewById<ListView>(R.id.lvStores)

        val coordinateList = mutableListOf<String>()

        val uAddress = getUserAddress()
        Log.i(TAG, "Returned user address: $uAddress")

        //val (userLat, userLong) = convertAddressToCoordinates(uAddress)
        //Log.w(TAG, "User Lat, Long: $userLat, $userLong")

        db.collection("Store chains")
            .get()
            .addOnSuccessListener { chainList ->
                for (document in chainList) {
                    storeChainList.add(document.id)
                }
                for(chainName in storeChainList)
                {
                    db.collection("store list")
                        .document("Butik info")
                        .collection("$chainName")
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result)
                            {
                                storeList.add(document.id)
                                storeAdressList.add(document.getString("Adress") ?: "default")
                                val tempCoord = document.getString("Coordinates") ?: "default"

                                if(tempCoord != "default")
                                {
                                    coordinateList.add(tempCoord)
                                    Log.i(TAG, "Address: ${storeAdressList.last()}, coordinates: ${coordinateList.last()}")
                                    val (storeLat, storeLong) = convertAddressToCoordinates(storeAdressList.last())
                                    Log.i(TAG, "Store Lat, Long: ${storeLat}, ${storeLong}")
                                }
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

    private fun convertAddressToCoordinates(address: String): Pair<String, String>
    {
        val geocode = Geocoder(this, Locale.getDefault())
        val addressList = geocode.getFromLocationName(address, 1)

        val lat = String.format("%.5f", addressList.get(0).latitude)
        val long = String.format("%.4f", addressList.get(0).longitude)

        //Log.i(TAG, "Latitude: ${lat}, Longitude: ${lng}")
        return Pair(lat, long)
    }
    fun getUserAddress(): String
    {
        Log.i(TAG, "Start of getUserAddress")
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        Log.i(TAG, "User id: ${uid}")

        var userAddress = ""
        val db = Firebase.firestore
        db.collection("users").document("$uid")
            .get()
            .addOnSuccessListener { document ->
                userAddress = document.getString("Adress") ?: "default"
                Log.i(TAG, "userAddress: $userAddress")
                Log.i(TAG, "Middle of getUserAddress, in SuccessListener")

            }
            .addOnFailureListener {
                Log.i(TAG, "Error: FailureListener")
            }

        Log.i(TAG, "End of getUserAddress")
        return userAddress
    }

}
