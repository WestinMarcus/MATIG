package com.example.groceryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodItemAdapter(private val foodList : List<FoodItem>) : RecyclerView.Adapter<FoodItemAdapter.MyViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position : Int)

    }

    fun setOnItemClickListener(listener: onItemClickListener){

        mListener = listener

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.shopping_list_item, parent, false)

        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = foodList[position]
        holder.itemName.text = currentItem.item
        holder.itemPrice.text = currentItem.price
    }

    override fun getItemCount(): Int {

        return foodList.size
    }

    class MyViewHolder(itemView : View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val itemName : TextView = itemView.findViewById(R.id.tv_itemName)
        val itemPrice : TextView = itemView.findViewById(R.id.tv_itemPrice)

        init {

            itemView.setOnClickListener{

                listener.onItemClick(adapterPosition)

            }
        }
    }
}