package com.example.recyclerbase.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerbase.R
import com.example.recyclerbase.model.Affirmation

class Adapter(private val context: Context, private val dataset: List<Affirmation>)
    : RecyclerView.Adapter<Adapter.RecyclerViewHolder>()
{

    class RecyclerViewHolder(private val view:View):RecyclerView.ViewHolder(view)
    {
        val textView: TextView = view.findViewById(R.id.title_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
        return RecyclerViewHolder(itemView)
    }

    override fun getItemCount(): Int {


        return dataset.size

    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val item = dataset[position]
        holder.textView.text =  context.resources.getString(item.stringResourceId)
    }


}