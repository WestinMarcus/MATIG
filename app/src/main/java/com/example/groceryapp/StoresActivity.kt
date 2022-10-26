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
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import com.google.firestore.admin.v1.Index
import kotlinx.coroutines.selects.select
import java.util.*

class StoresActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stores)

        val backBtn = findViewById<ImageView>(R.id.image_back_settings)
        backBtn.setOnClickListener{
            finish()
        }

        val sBtn : Button = findViewById(R.id.button2)

        fillFields()

        sBtn.setOnClickListener(){
            mainFun()
        }
    }

    private fun fillFields(){

        val db = Firebase.firestore

        val adresss : TextView = findViewById(R.id.editText)
        val city : TextView = findViewById(R.id.editText2)

        val user = Firebase.auth.currentUser
        val uid = user?.uid

        var userAdress = ""
        var userCity = ""


        db.collection("users").document("$uid")
        .get()
        .addOnSuccessListener { document ->

            userAdress = document.getString("Adress") ?: "default"
            userCity = document.getString("City") ?: "default"

            adresss.setText(userAdress).toString()
            city.setText(userCity).toString()
        }
    }

    private fun mainFun(){
        val db = Firebase.firestore
        val storeChainList = mutableListOf<String>()
        val storeList = mutableListOf<String>()
        val storeAdressList = mutableListOf<String>()
        var arrayAdapter: CustomAdapter
        val mListView = findViewById<ListView>(R.id.lvStores)
        var userAddress = ""
        val storeDistances = mutableListOf<Pair<String, String>>()

        val adresss : TextView = findViewById(R.id.editText)
        val city : TextView = findViewById(R.id.editText2)


        val adresss1 = adresss.text.toString()
        val city1 = city.text.toString()

        userAddress = "$adresss1, $city1"

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
                            storeAdressList.add(store.getString("Adress") ?: "default")

                            val (storeLat, storeLong) = convertAddressToCoordinates(storeAdressList.last())
                            val distance = calculateDistance(Pair(userLat, userLong), Pair(storeLat, storeLong))

                            storeList.add(" "+distance+"m -\t"+ store.id)
                            //Log.i(TAG, "Distance from user to ${storeList.last()}: ${distance}m")
                        }
                        storeList.sort()
                        mListView.adapter = CustomAdapter(this, storeList)
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
            var selectedStore = parent.getItemAtPosition(position)
            selectedStore = removeDistance(selectedStore.toString())
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
    private fun removeDistance(address: String): String
    {
        val index = address.indexOf("-")

        return address.substring(index+2) //+2 = " /t"
    }
}

