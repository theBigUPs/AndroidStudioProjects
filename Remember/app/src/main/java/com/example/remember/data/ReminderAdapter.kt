package com.example.remember.data

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.remember.R
import com.example.remember.models.Item
import com.example.remember.models.MainViewModel

class ReminderAdapter (private val itemList: List<Item>,private val viewModel: MainViewModel ,private val itemClickListener: ((Item) -> Unit)? = null, private val itemLongClickListener: ((Item) -> Unit)? = null) : RecyclerView.Adapter<ReminderAdapter.ViewHolder>() {
    private var isViewVisible = false




    init {
        viewModel.isBooleanLiveData.observeForever { newValue ->
            // Update the visibility of the delete RadioButton
            isViewVisible = !newValue
            isViewVisible = !isViewVisible

            // Notify adapter that the data has changed
            notifyItemRangeChanged(0, itemCount)
            //viewModel.updateBooleanValue(true)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        // Bind other data from currentItem to appropriate views
        holder.title.text = currentItem.title
        holder.time.text=currentItem.time

        holder.delete.visibility = if (isViewVisible) View.VISIBLE else View.GONE

        holder.delete.isChecked = false

        holder.itemView.setOnLongClickListener {
            //holder.delete.isChecked = false

            // Toggle the visibility state of the view
            if(holder.delete.visibility==View.GONE)
            {

                // Notify adapter that the data has changed
                notifyItemRangeChanged(0, itemCount)
                viewModel.updateBooleanValue(true)
            }



            true // Return true to indicate that the long click is consumed
        }




    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Declare and initialize other views here
        val title: TextView = itemView.findViewById(R.id.title_tv)
        val time: TextView = itemView.findViewById(R.id.time_tv)
        val delete: RadioButton = itemView.findViewById(R.id.delete_rb)

        init {





            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = itemList[position]
                    itemClickListener?.invoke(clickedItem)
                    if(delete.visibility == View.VISIBLE)
                    {
                        delete.isChecked = !delete.isChecked
                    }


                    if(delete.isChecked)
                    {
                        viewModel.addRemoveList(position)

                    }
                    else
                    {
                        //Log.e("unchecked",position.toString())
                        viewModel.deleteFromRemoveList(position)
                    }



                }
            }







        }




    }
}