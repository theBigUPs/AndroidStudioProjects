package com.example.remember.models


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.remember.data.DataSaveAndLoad
import com.example.remember.data.ScheduleReminder
import java.util.UUID
import java.util.concurrent.TimeUnit


class DataAccessViewModel : ViewModel() {

    private var itemListLiveData = MutableLiveData<MutableList<Item>>()

    private val updateAdapterLiveData = MutableLiveData<Boolean>()
    private val dataSaveAndLoad: DataSaveAndLoad
    init
    {
        itemListLiveData.value = mutableListOf()
        dataSaveAndLoad = DataSaveAndLoad(this)
    }


    val isUpdateAdapterBooleanLiveData: LiveData<Boolean>
        get() = updateAdapterLiveData

    // Function to update the boolean value
    fun updateAdapterBooleanValue(newValue: Boolean) {
        updateAdapterLiveData.value = newValue
    }


    val getItemListLiveData: LiveData<MutableList<Item>>
        get() = itemListLiveData





    fun loadData(context:Context)
    {
        dataSaveAndLoad.loadItemList(context)
    }

    fun saveData(context:Context)
    {
        dataSaveAndLoad.saveItemList(context)
    }

}