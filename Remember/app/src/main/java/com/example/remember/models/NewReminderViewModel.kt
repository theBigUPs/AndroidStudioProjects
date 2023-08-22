package com.example.remember.models


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel




class NewReminderViewModel : ViewModel() {

    private var itemListLiveData = MutableLiveData<MutableList<Item>>()

    private val updateAdapterLiveData = MutableLiveData<Boolean>()

    init
    {
        itemListLiveData.value = mutableListOf()
    }


    val isUpdateAdapterBooleanLiveData: LiveData<Boolean>
        get() = updateAdapterLiveData

    // Function to update the boolean value
    fun updateAdapterBooleanValue(newValue: Boolean) {
        updateAdapterLiveData.value = newValue
    }


    val getItemListLiveData: LiveData<MutableList<Item>>
        get() = itemListLiveData



}