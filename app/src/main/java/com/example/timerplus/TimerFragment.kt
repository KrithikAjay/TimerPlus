package com.example.timerplus

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class TimerFragment : Fragment() {
//    var timerStart: Boolean = false
//    var time = 0.0
//    lateinit var timerTask: TimerTask
    var timerText: TextView? = null



    companion object {
        fun newInstance() = TimerFragment()
    }

    private lateinit var viewModel: TimerViewModel

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v : View =  inflater.inflate(R.layout.timer_fragment, container, false)

        val actionBarToolbar : Toolbar = v.findViewById(R.id.toolbar);

        val application = requireNotNull(this.activity).application

        val dataSource = HistoryDatabase.getInstance(application).historyDatabaseDao

        val TimerViewModelFactory = TimerViewModelFactory(dataSource, application)

        viewModel =
            ViewModelProvider(
                this,TimerViewModelFactory ).get(TimerViewModel::class.java)
        viewModel.onStartTracking()


        actionBarToolbar.setLogo(R.drawable.ic_timer)
        actionBarToolbar.title = " TimerPlus"
        actionBarToolbar.inflateMenu(R.menu.menu_main);
        val menu =v.findViewById<View>(R.id.action_settings)
        setHasOptionsMenu(true)
        menu.setOnClickListener {
            v.findNavController().navigate(R.id.action_timerFragment_to_bgTimerFragment)

        }


        val startButton = v.findViewById<FloatingActionButton>(R.id.fab_start)
        val stopButton = v.findViewById<FloatingActionButton>(R.id.fab_stop)
        val pauseButton =v. findViewById<FloatingActionButton>(R.id.fab_pause)
        timerText = v.findViewById<TextView>(R.id.timer_text)




        val progressBar = v.findViewById<ProgressBar>(R.id.progress_circular)
        startButton.setOnClickListener {
             viewModel.startTimer()

             progressBar.isVisible = true
            Toast.makeText(
                requireContext(),
                "Started!!",
                Toast.LENGTH_SHORT
            ).show()






        }
        pauseButton.setOnClickListener {
            viewModel.pauseTimer()
            Toast.makeText(requireContext(), "Paused", Toast.LENGTH_SHORT).show()

            progressBar.isVisible = false


        }
        stopButton.setOnClickListener {
            val resetAlert = AlertDialog.Builder(requireContext())
            resetAlert.setTitle("Reset Timer")
            resetAlert.setMessage("Are you sure you want to reset the timer?")
            resetAlert.setPositiveButton("Reset") { dialogInterface, i ->
              viewModel.stopTimer()
              viewModel.onStopTracking()

                progressBar.isVisible = false
                Toast.makeText(requireContext(), "Reseted", Toast.LENGTH_SHORT).show()
            }
            resetAlert.setNeutralButton("Cancel") { dialogInterface, i ->
                //do nothing
            }
            resetAlert.show()
        }
 return v
    }

override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.timerValue.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            timerText?.text =  it
        })
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.itemId == R.id.action_settings) {
            return true
        } else {
            return super.onOptionsItemSelected(item)

        }
    }


}