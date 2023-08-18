package com.example.remember.models

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



    val getRemoveListLiveData: LiveData<MutableList<Int>>
        get() = removeListLiveData


    fun addRemoveList(newIndex:Int)
    {

        removeListLiveData.value?.apply {
            add(newIndex)
            removeListLiveData.value = this
        }

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