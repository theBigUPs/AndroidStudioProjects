package com.example.remember.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val setButtonVisibleLiveData = MutableLiveData<Boolean>()
    val isBooleanLiveData: LiveData<Boolean>
        get() = setButtonVisibleLiveData

    // Function to update the boolean value
    fun updateBooleanValue(newValue: Boolean) {
        setButtonVisibleLiveData.value = newValue
    }


}