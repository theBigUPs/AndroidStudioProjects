package com.example.remember.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.remember.R
import com.example.remember.data.Notification
import com.example.remember.data.ScheduleReminder
import com.example.remember.databinding.FragmentMainBinding
import com.example.remember.models.MainViewModel
import java.util.concurrent.TimeUnit

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val context: Context = requireContext()
        binding.newBtn.setOnClickListener {

            //var k = Notification()
            //k.showNotification(context, "Notification Title", "This is the notification message.")
            findNavController().navigate(R.id.action_mainFragment_to_newReminderFragment)




        }


    }
}