package com.example.remember.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkManager
import com.example.remember.R
import com.example.remember.data.ReminderAdapter
import com.example.remember.databinding.FragmentMainBinding
import com.example.remember.models.MainViewModel
import com.example.remember.models.DataAccessViewModel
import com.example.remember.models.ScheduleViewModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private val sharedViewModel: DataAccessViewModel by activityViewModels()
    private val scheduleViewModel: ScheduleViewModel by viewModels()
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


        binding.remindersRcv.layoutManager = LinearLayoutManager(context)
        val reminderAdapter = sharedViewModel.getItemListLiveData.value?.let { ReminderAdapter(it, viewModel) }
        binding.remindersRcv.adapter = reminderAdapter



        binding.newBtn.setOnClickListener {

            //var k = Notification()
            //k.showNotification(context, "Notification Title", "This is the notification message.")
            findNavController().navigate(R.id.action_mainFragment_to_newReminderFragment)
            //itemList.remove(itemList[itemList.size-1])
            //reminderAdapter.notifyItemRemoved(itemList.size )

        }

        viewModel.isBooleanLiveData.observe(viewLifecycleOwner) { newValue ->

            if (newValue) {

                binding.cancelMainFragmentBtn.visibility = View.VISIBLE
                binding.removeBtn.visibility = View.VISIBLE

            }
            else
            {

                binding.cancelMainFragmentBtn.visibility = View.GONE
                binding.removeBtn.visibility = View.GONE

            }
        }


        sharedViewModel.isUpdateAdapterBooleanLiveData.observe(viewLifecycleOwner)
        {newValue->
            if (newValue)
            {
                reminderAdapter?.notifyItemInserted(sharedViewModel.getItemListLiveData.value?.size!!-1)
                sharedViewModel.updateAdapterBooleanValue(false)
            }
        }



        binding.cancelMainFragmentBtn.setOnClickListener {

            viewModel.updateBooleanValue(false)
            viewModel.clearRemoveList()

        }

        binding.removeBtn.setOnClickListener {
            //need to destroy the scheduled workers as well
            val remList = viewModel.getRemoveListLiveData.value
            remList?.let {
                it.sortDescending()
                for(index in it)
                {

                    val id = sharedViewModel.getItemListLiveData.value?.get(index)?.workerId

                    if (id != null) {
                        scheduleViewModel.cancelWorker(context,id)
                    }
                    sharedViewModel.getItemListLiveData.value?.removeAt(index)
                    reminderAdapter?.notifyItemRemoved(index)
                }

            }
            viewModel.clearRemoveList()
            viewModel.updateBooleanValue(false)

        }
    }
}