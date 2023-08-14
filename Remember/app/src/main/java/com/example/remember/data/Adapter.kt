package com.example.remember.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.remember.R
import com.example.remember.models.Item

class Adapter (private val itemList: List<Item>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        //holder.textTitle.text = currentItem.title
        // Bind other data from currentItem to appropriate views
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        // Declare and initialize other views here
    }
}