package com.example.volumeadjuster


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        val serviceIntent = Intent(this, ForegroundService::class.java)
        startService(serviceIntent)

        finish()
    }

}