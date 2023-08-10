package com.example.remember.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.remember.R
import com.example.remember.databinding.FragmentMainBinding
import com.example.remember.databinding.FragmentNewReminderBinding
import com.example.remember.models.NewReminderViewModel
import java.util.Calendar

class NewReminderFragment : Fragment() {

    private lateinit var binding : FragmentNewReminderBinding

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



        binding.timeTp.setIs24HourView(true)
        binding.timeTp.setOnTimeChangedListener { _, hourOfDay, minute ->
            // Retrieve the selected hour and minute
            val currentTime = Calendar.getInstance()

            // Set the selected time from the TimePicker
            val selectedTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
            }


            val timeDifferenceMillis = selectedTime.timeInMillis - currentTime.timeInMillis

            // Convert milliseconds to hours and minutes
            val hours = timeDifferenceMillis / (60 * 60 * 1000)
            val minutes = (timeDifferenceMillis % (60 * 60 * 1000)) / (60 * 1000)

            // Display the time difference
            Log.e("time","Time difference: $hours hours and $minutes minutes")
            Log.e("time","Time :$hourOfDay and $minute minutes")

        }

        binding.calendarCv.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "${year}-${month + 1}-${dayOfMonth}" // Month is 0-based
            // Handle the selected date as needed

            
        }
        
        binding.createBtn.setOnClickListener {

            findNavController().navigate(R.id.action_newReminderFragment_to_mainFragment)

        }

        binding.cancelBtn.setOnClickListener {

            findNavController().navigate(R.id.action_newReminderFragment_to_mainFragment)

        }

    }

}