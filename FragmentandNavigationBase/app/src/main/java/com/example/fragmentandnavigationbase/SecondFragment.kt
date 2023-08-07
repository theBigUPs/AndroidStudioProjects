package com.example.fragmentandnavigationbase

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fragmentandnavigationbase.databinding.FragmentMainBinding
import com.example.fragmentandnavigationbase.databinding.FragmentSecondFragmentBinding

class SecondFragment : Fragment() {
    private lateinit var binding : FragmentSecondFragmentBinding
    companion object {
        fun newInstance() = SecondFragment()
    }

    private lateinit var viewModel: SecondFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecondFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SecondFragmentViewModel::class.java]

        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_secondFragment_to_mainFragment)
        }


    }

}