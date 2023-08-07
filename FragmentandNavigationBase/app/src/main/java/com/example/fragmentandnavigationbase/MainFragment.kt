package com.example.fragmentandnavigationbase

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fragmentandnavigationbase.databinding.FragmentMainBinding

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

        binding.qwe.text = "binding works"

        binding.nextBtn.setOnClickListener {

            findNavController().navigate(R.id.action_mainFragment_to_secondFragment)
            //the press of this button will not call onDestroy but it will call onDestroyView

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        Log.e("onDestroyView is called on MainFragment","rest in peace sweet prince")


    }
}