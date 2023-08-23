package com.example.remember.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.remember.R
import com.example.remember.databinding.FragmentNewReminderBinding
import com.example.remember.models.Item
import com.example.remember.models.DataAccessViewModel
import com.example.remember.models.ScheduleViewModel
import java.util.Calendar

class NewReminderFragment : Fragment() {

    private lateinit var binding : FragmentNewReminderBinding
    private var timeDifferenceMillis = 0L

    companion object {
        fun newInstance() = NewReminderFragment()
    }

    private val sharedViewModel: DataAccessViewModel by activityViewModels()
    private val scheduleViewModel:ScheduleViewModel by viewModels()

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

                val workRequestId = scheduleViewModel.scheduleWorker(timeDifferenceMillis,message,context)

                val newItem = Item(message, "$hour:$minute",workRequestId)
                sharedViewModel.getItemListLiveData.value?.add(newItem)
                sharedViewModel.updateAdapterBooleanValue(true)
                findNavController().navigate(R.id.action_newReminderFragment_to_mainFragment)
            }



        }

        binding.cancelBtn.setOnClickListener {

            findNavController().navigate(R.id.action_newReminderFragment_to_mainFragment)

        }

    }

}