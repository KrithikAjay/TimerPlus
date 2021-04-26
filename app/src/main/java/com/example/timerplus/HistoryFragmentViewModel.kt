package com.example.timerplus

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryFragmentViewModel(val database: HistoryDatabaseDao,
                               application: Application
) : AndroidViewModel(application){

   var history = History()
    private var recentTime = MutableLiveData<History?>()
    val alltimings = database.getAlltime()
    val alltimingsString = Transformations.map(alltimings) { alltimings ->
        formatTimer(alltimings, application.resources)
    }
    val clearButtonVisible = Transformations.map(alltimings) {
        it?.isNotEmpty()
    }
    private var _showSnackbarEvent = MutableLiveData<Boolean>()
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent
    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }
    init {
        initializerecentTime()
    }
    private fun initializerecentTime() {
        viewModelScope.launch {
            recentTime.value = getrecentTimeFromDatabase()
        }
    }


    private suspend fun getrecentTimeFromDatabase(): History? {
//        return withContext(Dispatchers.IO) {
        var time = database.getThisTime()
        if (time?.endTimeMilli != time?.startTimeMilli) {
            time = null
        }
        return time

    }


    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    private suspend fun update(night: History) {
        withContext(Dispatchers.IO) {
            database.update(night)
        }
    }

    private suspend fun insert(night: History) {
        withContext(Dispatchers.IO) {
            database.insert(night)
        }
    }

    /**
     * Executes when the START button is clicked.
     */
    fun onStartTracking() {
        viewModelScope.launch {
            // Create a new night, which captures the current time,
            // and insert it into the database.


            history.startTimeMilli = System.currentTimeMillis()
        }
    }

    /**
     * Executes when the STOP button is clicked.
     */
    fun onStopTracking() {
        viewModelScope.launch {

            history.endTimeMilli = System.currentTimeMillis()

            insert(history)

            // Set state to navigate to the SleepQualityFragment.

        }
    }

    /**
     * Executes when the CLEAR button is clicked.
     */
    fun onClear() {
        viewModelScope.launch {
            // Clear the database table.
            clear()

            // And clear recentTime since it's no longer in the database
            recentTime.value = null
        }

        // Show a snackbar message, because it's friendly.
        _showSnackbarEvent.value = true
    }


}

