package com.example.volumeadjuster

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        val serviceIntent = Intent(this, ForegroundService::class.java)
        startService(serviceIntent)


        //val filter = IntentFilter("close_app_action")
        //LocalBroadcastManager.getInstance(this).registerReceiver(closeAppReceiver, filter)
        finish()
    }



    override fun onDestroy() {
        super.onDestroy()

        //LocalBroadcastManager.getInstance(this).unregisterReceiver(closeAppReceiver)
    }




}