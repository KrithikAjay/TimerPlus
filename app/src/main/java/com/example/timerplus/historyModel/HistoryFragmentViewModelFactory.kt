package com.example.timerplus.historyModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.timerplus.database.HistoryRepository


/**
 * This is pretty much boiler plate code for a ViewModel Factory.
 *
 * Provides the SleepDatabaseDao and context to the ViewModel.
 */
class HistoryFragmentViewModelFactory(
        private val repository: HistoryRepository,
        val application: Application

        ) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryFragmentViewModel::class.java)) {
            return HistoryFragmentViewModel(repository, application ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}