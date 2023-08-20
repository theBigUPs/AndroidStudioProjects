package com.example.remember.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewReminderViewModel : ViewModel() {

    private var itemListLiveData = MutableLiveData<MutableList<Item>>()


    init
    {
        itemListLiveData.value = mutableListOf()
    }

    val getItemListLiveData: LiveData<MutableList<Item>>
        get() = itemListLiveData


    fun addItemList(newItem:Item)
    {
        itemListLiveData.value?.add(newItem)
        //adapter.notifyItemInserted(itemList.size - 1)

    }

    fun removeItemList(itemIndex:Item)
    {
        //schedule worker gets cancelled here as well
    }

    fun saveItemList()
    {
        //convert list to json and save it here
    }

    fun loadItemList()
    {
        //load json and assign it to livedata
    }
}