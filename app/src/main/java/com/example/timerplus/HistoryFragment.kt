package com.example.timerplus

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.renderscript.ScriptGroup
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
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

        val dataSource = HistoryDatabase.getInstance(application).historyDatabaseDao

        val viewModelFactory = HistoryFragmentViewModelFactory(dataSource, application)

        val historyViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(HistoryFragmentViewModel::class.java)

        binding.historyViewModel = historyViewModel

        binding.lifecycleOwner = this
        historyViewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                // Reset state to make sure the snackbar is only shown once, even if the device
                // has a configuration change.
                historyViewModel.doneShowingSnackbar()
            }
        })


        return binding.root
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(HistoryFragmentViewModel::class.java)
//        // TODO: Use the ViewModel
//    }


}