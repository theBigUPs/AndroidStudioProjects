package com.example.remember.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.remember.R
import com.example.remember.data.ScheduleReminder
import com.example.remember.databinding.FragmentNewReminderBinding
import com.example.remember.models.NewReminderViewModel
import java.util.Calendar
import java.util.concurrent.TimeUnit

class NewReminderFragment : Fragment() {

    private lateinit var binding : FragmentNewReminderBinding
    private var timeDifferenceMillis = 0L

    companion object {
        fun newInstance() = NewReminderFragment()
    }

    private lateinit var viewModel: NewReminderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[NewReminderViewModel::class.java]
        val context: Context = requireContext()
        var calenderSelected = false
        var timeSelected = false


        binding.timeTp.setIs24HourView(true)
        binding.timeTp.setOnTimeChangedListener { _, hourOfDay, minute ->
            // Retrieve the selected hour and minute
            timeSelected = true
            val currentTime = Calendar.getInstance()

            // Set the selected time from the TimePicker
            val selectedTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
            }


            timeDifferenceMillis = selectedTime.timeInMillis - currentTime.timeInMillis

            // Convert milliseconds to hours and minutes
            //var hours = timeDifferenceMillis / (60 * 60 * 1000)
            //var minutes = (timeDifferenceMillis % (60 * 60 * 1000)) / (60 * 1000)

            // Display the time difference
            //Log.e("time","Time difference: $hours hours and $minutes minutes")
            //Log.e("time","Time :$hourOfDay and $minute minutes")

            if (timeDifferenceMillis < 0)
            {
                timeDifferenceMillis *= -1
            }


        }


        
        binding.createBtn.setOnClickListener {

            if(timeSelected)
            {


                var message = binding.remindertextEt.text.toString()
                if(message=="null")
                {
                    message=""
                }
                Log.d("message",binding.remindertextEt.text.toString())
                val inputData = Data.Builder()
                    .putString("worker_key", message)
                    .build()


                //Log.d("seconds",(timeDifferenceMillis/1000).toString())
                val workRequest = OneTimeWorkRequest.Builder(ScheduleReminder::class.java)
                    .setInputData(inputData)
                    .setInitialDelay(timeDifferenceMillis, TimeUnit.MILLISECONDS)
                    .build()
                WorkManager.getInstance(context).enqueue(workRequest)
                val workRequestId = workRequest.id
            }

            //findNavController().navigate(R.id.action_newReminderFragment_to_mainFragment)

        }

        binding.cancelBtn.setOnClickListener {

            findNavController().navigate(R.id.action_newReminderFragment_to_mainFragment)

        }







    }

}