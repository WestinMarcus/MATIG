package com.example.groceryapp

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
class ShoppingListAdapter(private val context: Activity, private val item: List<String>, private val price: List<String>)
        : ArrayAdapter<String>(context, R.layout.shopping_list_item, item) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.shopping_list_item, null, true)

        val itemText = rowView.findViewById(R.id.tv_itemName) as TextView
        val itemPrice = rowView.findViewById(R.id.tv_itemPrice) as TextView

        itemText.text = item[position]
        itemPrice.text = price[position]

        return rowView
    }
}