package com.example.remember.models

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.remember.data.ScheduleReminder
import java.util.UUID
import java.util.concurrent.TimeUnit

class ScheduleViewModel : ViewModel(){


    fun scheduleWorker(timeDifferenceMillis: Long, message: String, context: Context): UUID {
        val inputData = Data.Builder()
            .putString("worker_key", message)
            .build()


        //Log.d("seconds",(timeDifferenceMillis/1000).toString())
        val workRequest = OneTimeWorkRequest.Builder(ScheduleReminder::class.java)
            .setInputData(inputData)
            .setInitialDelay(timeDifferenceMillis, TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)
        return workRequest.id
    }


    fun cancelWorker(context: Context,id:UUID)
    {

        WorkManager.getInstance(context).cancelWorkById(id)

    }


}