package com.example.remember.data

import android.content.Context
import com.example.remember.models.Item
import com.example.remember.models.DataAccessViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataSaveAndLoad(private val viewModel: DataAccessViewModel) {


    fun saveItemList(context: Context)
    {
        val gson = Gson()
        val jsonList = gson.toJson(viewModel.getItemListLiveData.value)

        // Save the JSON string to SharedPreferences
        val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("list_key", jsonList)
        editor.apply()
    }

    fun loadItemList(context: Context)
    {
        val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        //load json and assign it to livedata
        val jsonList = sharedPreferences.getString("list_key", null)

        // Convert the JSON string back to a list using Gson
        val gson = Gson()
        val listType = object : TypeToken<MutableList<Item>>() {}.type
        val yourList = gson.fromJson<MutableList<Item>>(jsonList, listType)
        //var itemListLiveData = MutableLiveData<MutableList<Item>>()
        if(yourList!=null)
        {
            viewModel.getItemListLiveData.value?.addAll(yourList)
        }

    }





}