package com.example.groceryapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import java.util.*
import kotlinx.coroutines.*
import kotlin.collections.List
import kotlin.system.*

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

        val user = Firebase.auth.currentUser
        val uid = user?.uid

        var userAddress = ""

        /*-----------------Retrieves user address from Firestore----------------------*/
        runBlocking{
            launch(Dispatchers.IO) {
                db.collection("users").document("$uid")
                .get()
                .addOnSuccessListener { userDoc ->
                    val uCity = userDoc.getString("City") ?: "Default"
                    userAddress = userDoc.getString("Adress") ?: "Default"
                    userAddress += ", $uCity"

                }
                .addOnFailureListener {
                    Log.i(TAG, "FailureListener in runBlocking")
                }
            }
        }

        /*---------------------- Gets list of store chain names ----------------------*/
        db.collection("Store chains")
        .get()
        .addOnSuccessListener { chainList ->
            for (document in chainList) {
                storeChainList.add(document.id)
            }
            val (userLat, userLong) = convertAddressToCoordinates(userAddress)
            for(chainName in storeChainList)
            {
                /*---------------Gets list of all store from all chains---------------*/
                db.collection("store list")
                .document("Butik info")
                .collection("$chainName")
                .get()
                .addOnSuccessListener { stores ->
                    for (store in stores)
                    {
                        storeList.add(store.id)
                        storeAdressList.add(store.getString("Adress") ?: "default")
                        // MAKE NEW LIST AND SAVE DISTANCE TO ALL STORES

                        /*------------converts address to coordinates for latest store in storelist--------------*/
                        val (storeLat, storeLong) = convertAddressToCoordinates(storeAdressList.last())
                        val distance = calculateDistance(Pair(userLat, userLong), Pair(storeLat, storeLong))
                        Log.i(TAG, "Distance from user to ${storeList.last()}: ${distance}m")
                    }
                    arrayAdapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, storeList)
                    mListView.adapter = arrayAdapter
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents.", exception)
                }
            }
        }
        .addOnFailureListener {
            Log.e(TAG, "Error: FailureListener")
        }
        mListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedStore = parent.getItemAtPosition(position)
            val intent = Intent(this, ItemListActivity::class.java)
            intent.putExtra("store", "$selectedStore")

            startActivity(intent)
        }
    }

    private fun calculateDistance(userCoord: Pair<String, String>, storeCoord: Pair<String, String>): Int
    {
        val startPoint = Location("User")
        startPoint.latitude = userCoord.first.toDouble()
        startPoint.longitude = userCoord.second.toDouble()

        val endPoint = Location("Store")
        endPoint.latitude = storeCoord.first.toDouble()
        endPoint.longitude = storeCoord.second.toDouble()

        val distance = startPoint.distanceTo(endPoint).toDouble()

        return distance.toInt()
    }

    private fun convertAddressToCoordinates(address: String): Pair<String, String>
    {


        val geocode = Geocoder(this, Locale.getDefault())

        val addressList = geocode.getFromLocationName(address, 1)

        val lat = String.format("%.5f", addressList.first().latitude)
        val long = String.format("%.4f", addressList.first().longitude)

        return Pair(lat, long)
    }

}

