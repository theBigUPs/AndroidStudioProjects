package com.example.recyclerbase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerbase.adapter.Adapter
import com.example.recyclerbase.data.DataSource
import com.example.recyclerbase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val myDataset = DataSource().loadAffirmations()


        val stringList = mutableListOf<String>()

        // Add elements to the list
        stringList.add("Apple")
        stringList.add("Banana")
        stringList.add("Orange")



        binding.rec.adapter = Adapter(this, myDataset)
        binding.rec.layoutManager= LinearLayoutManager(this)




    }
}