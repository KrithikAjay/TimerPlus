package com.example.timerplus.historyModel

import android.app.Application
import androidx.lifecycle.*
import com.example.timerplus.database.History
import com.example.timerplus.database.HistoryRepository
import com.example.timerplus.formatTimer
import kotlinx.coroutines.launch

class HistoryFragmentViewModel(private val repository: HistoryRepository,
                               application: Application
                               ) : ViewModel(){
    var history = History()
    private var recentTime = MutableLiveData<History?>()
    private val allTimings = repository.getAllTime
    val allTimingsString = Transformations.map(allTimings) { allTimings ->
        formatTimer(allTimings, application.resources)
    }

    private val _showSnackbarEvent = MutableLiveData<Boolean>()
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
        var time = repository.getThisTime()
        if (time?.endTimeMilli != time?.startTimeMilli) {
            time = null
        }
        return time

    }


    fun onClear() {
        viewModelScope.launch {
        repository.clear()

            recentTime.value = null }
        // Show a snackbar message, because it's friendly.
        _showSnackbarEvent.value = true

    }

    fun update(night: History) {
       viewModelScope.launch {
            repository.update(night)
        }
    }
      fun insert(night: History) {
        viewModelScope.launch {
        repository.insert(night)
        }
    }






}

