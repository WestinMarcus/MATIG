package com.example.groceryapp

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*

class CustomAdapter(private val context: Activity, private val item: List<String>)
    : ArrayAdapter<String>(context, R.layout.custom_list, item) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_list, null, true)
        val itemText = rowView.findViewById(R.id.tv_itemName) as TextView
        itemText.text = item[position]

        return rowView
    }
}