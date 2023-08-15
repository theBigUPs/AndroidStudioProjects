package com.example.remember.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.remember.R
import com.example.remember.models.Item

class ReminderAdapter (private val itemList: List<Item>) : RecyclerView.Adapter<ReminderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        // Bind other data from currentItem to appropriate views
        holder.title.text = currentItem.title
        holder.time.text=currentItem.time
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Declare and initialize other views here
        val title: TextView = itemView.findViewById(R.id.title_tv)
        val time: TextView = itemView.findViewById(R.id.time_tv)

    }
}