package com.example.remember.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val setButtonVisibleLiveData = MutableLiveData<Boolean>()

    private val removeListLiveData = MutableLiveData<MutableList<Int>>()

    val isBooleanLiveData: LiveData<Boolean>
        get() = setButtonVisibleLiveData

    // Function to update the boolean value
    fun updateBooleanValue(newValue: Boolean) {
        setButtonVisibleLiveData.value = newValue
    }


    init {
        removeListLiveData.value = mutableListOf() // Initialize with an empty list
    }

    val getRemoveListLiveData: LiveData<MutableList<Int>>
        get() = removeListLiveData


    fun addRemoveList(newIndex:Int)
    {

        val updatedList = removeListLiveData.value ?: mutableListOf()
        updatedList.add(newIndex)
        removeListLiveData.value = updatedList
        //Log.e("added", removeListLiveData.value!![0].toString())
    }

    fun deleteFromRemoveList(index:Int)
    {
        removeListLiveData.value?.apply {
            remove(index)
            removeListLiveData.value = this
        }
    }


    fun clearRemoveList()
    {
        removeListLiveData.value?.apply{
            clear()
            removeListLiveData.value = this
        }
    }


}