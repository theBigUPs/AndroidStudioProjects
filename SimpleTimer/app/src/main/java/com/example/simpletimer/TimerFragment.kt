package com.example.simpletimer


import android.content.Context

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer

import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

import com.example.simpletimer.databinding.FragmentTimerBinding


class TimerFragment : Fragment() {

    private lateinit var binding: FragmentTimerBinding

    private lateinit var viewModel: TimerViewModel


    private val updateIntervalMillis = 1000 // Update every 1 second
    var totalDurationMillis :Long = 3
    private lateinit var timer :CountDownTimer
    private var started=false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[TimerViewModel::class.java]




        binding.emptyingProgressBar.progress = 100


        binding.startBtn.setOnClickListener {
            totalDurationMillis = binding.secondsTn.text.toString().toLong()*1000
            //Toast.makeText(context, totalDurationMillis.toString(), Toast.LENGTH_SHORT).show()
            started=true


            binding.secondsTn.isFocusableInTouchMode=false
            binding.minutesTn.isFocusableInTouchMode=false
            binding.hoursTn.isFocusableInTouchMode=false

            timer = object : CountDownTimer(totalDurationMillis, updateIntervalMillis.toLong()) {




                override fun onTick(millisUntilFinished: Long) {
                    // Calculate progress based on time elapsed
                    val timeElapsedMillis = totalDurationMillis - millisUntilFinished
                    val progress = (timeElapsedMillis.toFloat() / totalDurationMillis) * 100
                    binding.emptyingProgressBar.progress = progress.toInt()
                }

                override fun onFinish() {
                    binding.emptyingProgressBar.progress = 100
                    val delayMillis:Long = 200

                    // Use view.postDelayed to schedule the code after the specified delay
                    binding.emptyingProgressBar.postDelayed({
                        val notification = MakeNotification()
                        context?.let { notification.showNotification(it, "times up") }

                        if (binding.loopSwitch.isChecked) {
                            timer.start()
                        }
                        else
                        {
                            binding.secondsTn.isFocusableInTouchMode=true
                            binding.minutesTn.isFocusableInTouchMode=true
                            binding.hoursTn.isFocusableInTouchMode=true
                            started = false
                        }
                    }, delayMillis)

                }
            }


            //timer.cancel()
            timer.start()

        }

        binding.cancelBtn.setOnClickListener{


            if(started)
            {
                binding.secondsTn.isFocusableInTouchMode=true
                binding.minutesTn.isFocusableInTouchMode=true
                binding.hoursTn.isFocusableInTouchMode=true
                started = false
                timer.cancel()
                binding.emptyingProgressBar.progress = 100
            }

        }



        binding.secondsTn.setOnEditorActionListener { thisTextView, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Hide the soft keyboard
                //(thisTextView as EditText).setSelection(thisTextView.text.length)
                //Toast.makeText(context, "here", Toast.LENGTH_SHORT).show()
                hideSoftKeyboard(thisTextView)
                thisTextView.clearFocus()

                if(thisTextView.text.length==1)
                {
                    thisTextView.text = "0"+thisTextView.text
                }

                if(thisTextView.text.isEmpty())
                {
                    thisTextView.text = "00"
                }
                true // Consume the event
            } else {
                false // Let the system handle other events
            }

        }

        binding.secondsTn.onFocusChangeListener = View.OnFocusChangeListener { thisView, hasFocus ->
            if (hasFocus) {
                (thisView as EditText).setText("")
            }
        }


        binding.minutesTn.setOnEditorActionListener { thisTextView, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Hide the soft keyboard

                hideSoftKeyboard(thisTextView)
                thisTextView.clearFocus()

                if(thisTextView.text.length==1)
                {
                    thisTextView.text = "0" + thisTextView.text
                }

                if(thisTextView.text.isEmpty())
                {
                    thisTextView.text = "00"
                }

                true // Consume the event
            } else {
                false // Let the system handle other events
            }


        }

        binding.minutesTn.onFocusChangeListener = View.OnFocusChangeListener { thisView, hasFocus ->
            if (hasFocus) {
                (thisView as EditText).setText("")
            }
        }


        binding.hoursTn.setOnEditorActionListener { thisTextView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Hide the soft keyboard

                hideSoftKeyboard(thisTextView)
                thisTextView.clearFocus()

                if(thisTextView.text.length==1)
                {
                    thisTextView.text = "0" + thisTextView.text
                }

                if(thisTextView.text.isEmpty())
                {
                    thisTextView.text = "00"
                }

                true // Consume the event
            } else {
                false // Let the system handle other events
            }

        }


        binding.hoursTn.onFocusChangeListener = View.OnFocusChangeListener { thisView, hasFocus ->
            if (hasFocus) {
                (thisView as EditText).setText("")
            }
        }





    }

    private fun hideSoftKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }








    override fun onDestroyView() {
        super.onDestroyView()

        Log.e("onDestroyView is called on TimerFragment","rest in peace sweet prince")


    }

}