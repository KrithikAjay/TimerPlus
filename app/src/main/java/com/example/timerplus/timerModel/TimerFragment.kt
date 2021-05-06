package com.example.timerplus.timerModel

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast

import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.timerplus.R
import com.example.timerplus.TimerViewModel
import com.example.timerplus.TimerViewModelFactory
import com.example.timerplus.database.HistoryDatabase
import com.example.timerplus.databinding.TimerFragmentBinding

class TimerFragment : Fragment() {
//    var timerStart: Boolean = false
//    var time = 0.0
//    lateinit var timerTask: TimerTask

    private lateinit var binding: TimerFragmentBinding



    companion object {
        fun newInstance() = TimerFragment()
    }

    private lateinit var viewModel: TimerViewModel

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.timer_fragment,container,false)




//        val actionBarToolbar : Toolbar = v.findViewById(R.id.toolbar);

        val application = requireNotNull(this.activity).application

        val dataSource = HistoryDatabase.getInstance(application).historyDatabaseDao

        val timerViewModelFactory = TimerViewModelFactory(dataSource, application)

        viewModel =
            ViewModelProvider(
                this,timerViewModelFactory ).get(TimerViewModel::class.java)
        viewModel.onStartTracking()

        viewModel.timerValue.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.timerText.text = it
        })
        binding.apply {
            toolbar.setLogo(R.drawable.ic_timer)
            toolbar.title = " TimerPlus"
            toolbar.inflateMenu(R.menu.menu_main);
//            if(toolbar.menu.getItem(0) .title == "history" )
//                findNavController().navigate(R.id.action_timerFragment_to_bgTimerFragment)


        }



        val menu = binding.root.findViewById<View>(R.id.history)
        setHasOptionsMenu(true)
        menu.setOnClickListener {
            findNavController().navigate(R.id.action_timerFragment_to_bgTimerFragment)

        }



        binding.startButton.setOnClickListener {
             viewModel.startTimer()

             binding.progressCircular.isVisible = true
            Toast.makeText(
                requireContext(),
                "Started!!",
                Toast.LENGTH_SHORT
            ).show()






        }
        binding.pauseButton.setOnClickListener {
            viewModel.pauseTimer()
            Toast.makeText(requireContext(), "Paused", Toast.LENGTH_SHORT).show()

            binding.progressCircular.isVisible = false


        }
                binding.stopButton.setOnClickListener {
            val resetAlert = AlertDialog.Builder(requireContext())
            resetAlert.setTitle("Reset Timer")
            resetAlert.setMessage("Are you sure you want to reset the timer?")
            resetAlert.setPositiveButton("Reset") { dialogInterface, i ->
              viewModel.stopTimer()
              viewModel.onStopTracking()

                binding.progressCircular.isVisible = false
                Toast.makeText(requireContext(), "Reseted", Toast.LENGTH_SHORT).show()
            }
            resetAlert.setNeutralButton("Cancel") { dialogInterface, i ->
                //do nothing
            }
            resetAlert.show()
        }
 return binding.root
    }




}