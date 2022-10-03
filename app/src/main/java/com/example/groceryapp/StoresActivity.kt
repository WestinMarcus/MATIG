package com.example.groceryapp

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
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
        var tempCoord = ""
        var coordinates = ""

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
                                tempCoord = document.getString("Coordinates") ?: "default"

                                if(tempCoord != "default")
                                {
                                    coordinateList.add(tempCoord)

                                    printAddressAndCoordinates(storeAdressList.last(), coordinateList.last())

                                    val (lat, lng) = convertAddressToCoordinates(storeAdressList)
                                    Log.w(TAG, "Converted Lat, Long: ${lat}, ${lng}")
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
    fun printAddressAndCoordinates(storeAddress: String, coordinates: String)
    {
        Log.w(TAG, "Address: ${storeAddress}, coordinates: ${coordinates}")
    }

    fun convertAddressToCoordinates(storeAddressList: List<String>): Pair<String, String>
    {
        val geocode = Geocoder(this, Locale.getDefault())
        val addressList = geocode.getFromLocationName(storeAddressList.last(), 1)

        var lat = String.format("%.5f", addressList.get(0).latitude)
        var lng = String.format("%.4f", addressList.get(0).longitude)

        //Log.i(TAG, "Latitude: ${lat}, Longitude: ${lng}")
        return Pair(lat, lng)
    }

}
/*for((index, storeName) in storeList.withIndex())
{
    Log.i(TAG, "index: $index is $storeName")
}*/