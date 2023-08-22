package com.example.remember.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.remember.R
import com.example.remember.data.ScheduleReminder
import com.example.remember.databinding.FragmentNewReminderBinding
import com.example.remember.models.Item
import com.example.remember.models.NewReminderViewModel
import java.util.Calendar
import java.util.concurrent.TimeUnit

class NewReminderFragment : Fragment() {

    private lateinit var binding : FragmentNewReminderBinding
    private var timeDifferenceMillis = 0L

    companion object {
        fun newInstance() = NewReminderFragment()
    }

    private val sharedViewModel: NewReminderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context: Context = requireContext()
        var calenderSelected = false
        var timeSelected = false
        lateinit var hour:String
        lateinit var minute:String

        binding.timeTp.setIs24HourView(true)
        binding.timeTp.setOnTimeChangedListener { _, hourOfDay, minutes ->
            // Retrieve the selected hour and minute
            timeSelected = true
            val currentTime = Calendar.getInstance()

            // Set the selected time from the TimePicker
            val selectedTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minutes)
                set(Calendar.SECOND, 0)
            }
            hour = hourOfDay.toString()
            minute = minutes.toString()

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


                val message = binding.remindertextEt.text.toString()
                //check if message empty if empty go no further and make a toast warning the user

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

                val newItem = Item(message, "$hour:$minute",workRequestId)
                sharedViewModel.getItemListLiveData.value?.add(newItem)
                sharedViewModel.updateAdapterBooleanValue(true)

            }

            findNavController().navigate(R.id.action_newReminderFragment_to_mainFragment)

        }

        binding.cancelBtn.setOnClickListener {

            findNavController().navigate(R.id.action_newReminderFragment_to_mainFragment)

        }







    }

}