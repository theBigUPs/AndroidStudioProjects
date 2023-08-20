package com.example.remember.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.remember.R
import com.example.remember.data.ReminderAdapter
import com.example.remember.databinding.FragmentMainBinding
import com.example.remember.models.Item
import com.example.remember.models.MainViewModel
import com.example.remember.models.NewReminderViewModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    companion object {
        fun newInstance() = MainFragment()
    }
    private var itemList: MutableList<Item> = mutableListOf()
    private lateinit var viewModel: MainViewModel
    private val sharedViewModel: NewReminderViewModel by activityViewModels()
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
        val reminderAdapter = ReminderAdapter(itemList, viewModel)
        binding.remindersRcv.adapter = reminderAdapter

        val newItem = Item("dinner","1",12)
        itemList.add(newItem)
        //adapter.notifyItemInserted(itemList.size - 1)
        reminderAdapter.notifyItemInserted(itemList.size - 1)
        val newItem1 = Item("dinner","2",12)
        itemList.add(newItem1)
        //adapter.notifyItemInserted(itemList.size - 1)
        reminderAdapter.notifyItemInserted(itemList.size - 1)

        val newItem2 = Item("dinner","3",12)
        itemList.add(newItem2)
        //adapter.notifyItemInserted(itemList.size - 1)
        reminderAdapter.notifyItemInserted(itemList.size - 1)

        val newItem3 = Item("dinner","4",12)
        itemList.add(newItem3)
        //adapter.notifyItemInserted(itemList.size - 1)
        reminderAdapter.notifyItemInserted(itemList.size - 1)
        val newItem4 = Item("dinner","5",12)
        itemList.add(newItem4)
        //adapter.notifyItemInserted(itemList.size - 1)
        reminderAdapter.notifyItemInserted(itemList.size - 1)


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
                    itemList.removeAt(index)
                    reminderAdapter.notifyItemRemoved(index)
                }

            }
            viewModel.clearRemoveList()
            viewModel.updateBooleanValue(false)

        }
    }
}