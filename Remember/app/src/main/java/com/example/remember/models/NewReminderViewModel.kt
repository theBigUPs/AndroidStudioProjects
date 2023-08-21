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



}