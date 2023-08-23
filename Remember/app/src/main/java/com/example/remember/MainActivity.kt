package com.example.remember

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.remember.models.DataAccessViewModel

class MainActivity : AppCompatActivity() {
    private val sharedViewModel: DataAccessViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}