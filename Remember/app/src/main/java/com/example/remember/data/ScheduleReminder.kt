package com.example.remember.data

import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class ScheduleReminder(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Background task code
        Log.d("task","Delayed task executed after 5 secs.")
        return Result.success()
    }



    //val workRequest = OneTimeWorkRequest.Builder(ScheduleReminder::class.java)
       // .setInitialDelay(5, TimeUnit.SECONDS) // Delay for 5 seconds
      //  .build()
    //WorkManager.getInstance(context).enqueue(workRequest)
    //this is how you summon it


}