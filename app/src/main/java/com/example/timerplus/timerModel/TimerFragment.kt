package com.example.timerplus.timerModel

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast

import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.timerplus.R
import com.example.timerplus.TimerViewModel
import com.example.timerplus.database.HistoryDatabase
import com.example.timerplus.databinding.TimerFragmentBinding

class TimerFragment : Fragment() {


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


        val application = requireNotNull(this.activity).application

        val dataSource = HistoryDatabase.getInstance(application).historyDatabaseDao

        val timerViewModelFactory = TimerViewModelFactory(dataSource, application)

        viewModel =
            ViewModelProvider(
                this,timerViewModelFactory ).get(TimerViewModel::class.java)
       binding.timerViewModel = viewModel
        binding.lifecycleOwner = this




        //Tool Bar
        binding.apply {
            toolbar.setLogo(R.drawable.ic_timer)
            toolbar.title = " TimerPlus"
            toolbar.inflateMenu(R.menu.menu_main);
//
        }

        //Navigation through meanu item
        val menu = binding.root.findViewById<View>(R.id.history)
        setHasOptionsMenu(true)
        menu.setOnClickListener {
            findNavController().navigate(R.id.action_timerFragment_to_bgTimerFragment)

        }

        //ProgressBarVisibility
        viewModel._progressBarVisible.observe(viewLifecycleOwner, Observer {
                 binding.progressCircular.isVisible = it
            })

        //Show Toast
        viewModel._showToast.observe(viewLifecycleOwner,
            Observer {
                when(it){
                    1 ->  {
                        Toast.makeText(requireContext(), "STARTED!!", Toast.LENGTH_SHORT).show()
                        viewModel.doneShowToast()
                    }
                    0 -> {
                        Toast.makeText(requireContext(), "PAUSED", Toast.LENGTH_SHORT).show()
                        viewModel.doneShowToast()
                    }

                }
            })

           //Reset value using AlertDialog
            binding.resetButton.setOnClickListener{
            val resetAlert = AlertDialog.Builder(requireContext())
            resetAlert.setTitle("Reset Timer")
            resetAlert.setMessage("Are you sure you want to reset the timer?")
            resetAlert.setPositiveButton("Reset") { dialogInterface, i ->
                viewModel.resetTimer()
                Toast.makeText(requireContext(), "RESETED", Toast.LENGTH_SHORT).show()
            }
            resetAlert.setNeutralButton("Cancel") { dialogInterface, i ->
                //do nothing
            }
            resetAlert.show()



        }




 return binding.root
    }









}