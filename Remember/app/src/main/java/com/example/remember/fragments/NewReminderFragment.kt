package com.example.remember.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.remember.R
import com.example.remember.databinding.FragmentMainBinding
import com.example.remember.databinding.FragmentNewReminderBinding
import com.example.remember.models.NewReminderViewModel

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


        binding.createBtn.setOnClickListener {

            findNavController().navigate(R.id.action_newReminderFragment_to_mainFragment)

        }

        binding.cancelBtn.setOnClickListener {

            findNavController().navigate(R.id.action_newReminderFragment_to_mainFragment)

        }

    }

}