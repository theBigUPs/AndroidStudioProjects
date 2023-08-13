package com.example.remember.data

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters


class ScheduleReminder(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Background task code

        val inputData = inputData.getString("worker_key")
        val applicationContext = applicationContext
        val notification = Notification()
        notification.showNotification(applicationContext,"reminder","executed after "+inputData.toString()+" seconds")
        return Result.success()
    }



    //val workRequest = OneTimeWorkRequest.Builder(ScheduleReminder::class.java)
       // .setInitialDelay(5, TimeUnit.SECONDS) // Delay for 5 seconds
      //  .build()
    //WorkManager.getInstance(context).enqueue(workRequest)
    //this is how you summon it


}