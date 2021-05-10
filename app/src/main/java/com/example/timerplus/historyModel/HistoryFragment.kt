package com.example.timerplus.historyModel

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.timerplus.HistoryApplication
import com.example.timerplus.R
import com.example.timerplus.databinding.HistoryFragmentBinding
import com.google.android.material.snackbar.Snackbar

class HistoryFragment : Fragment() {

    companion object {
        fun newInstance() = HistoryFragment()
    }

    private lateinit var viewModel: HistoryFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : HistoryFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.history_fragment, container, false)

        val application = requireNotNull(this.activity).application

        val repository = HistoryApplication().repository

        val viewModelFactory = HistoryFragmentViewModelFactory(repository, application)

        val historyViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(HistoryFragmentViewModel::class.java)

        binding.historyViewModel = historyViewModel

        binding.lifecycleOwner = this
        val adapter = HistoryAdapter()
        binding.historyList.adapter = adapter

        historyViewModel.allTimings.observe(viewLifecycleOwner, Observer {

            if(it.isEmpty()) Toast.makeText(context,"NO DATA TO DISPLAY",Toast.LENGTH_SHORT).show()
            it?.let { adapter.submitList(it)
            }

        })


        historyViewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_LONG// How long to display the message.
                ).show()
                // Reset state to make sure the snackbar is only shown once, even if the device
                // has a configuration change.
                historyViewModel.doneShowingSnackbar()
            }
        })



        return binding.root
    }






}