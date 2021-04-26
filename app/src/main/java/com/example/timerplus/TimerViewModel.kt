package com.example.timerplus

import android.app.Activity
import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.*

class TimerViewModel(val dataSource: HistoryDatabaseDao, application: Application) : ViewModel() {
    var history = History()
    var timerStart: Boolean = false
    var time = 0.0
    lateinit var timerTask: TimerTask
    var timerValue = MutableLiveData<String>()
    var timer: Timer? = null
    init {
        timer = Timer()
    }

   fun startTimer() {
       if (timerStart == false) {
           timerStart = true
           val activity = Activity()
//       val handler  = Handler(Looper.getMainLooper())

           timerTask = object : TimerTask() {
               override fun run() {
                   activity.runOnUiThread {
                       time++
                       timerValue!!.value = getTimerText()
                   }
               }
           }

           timer!!.scheduleAtFixedRate(timerTask, 0, 1000)
       }
    }
    fun getTimerText(): String {
        val rounded = Math.round(time).toInt()
        val seconds = rounded % 86400 % 3600 % 60
        val minutes = rounded % 86400 % 3600 / 60
        val hours = rounded % 86400 / 3600
        return formatTime(seconds, minutes, hours)
    }
    fun onStartTracking() {
        viewModelScope.launch {
            history.startTimeMilli = System.currentTimeMillis()
        }
    }

    /**
     * Executes when the STOP button is clicked.
     */
    fun onStopTracking() {
        viewModelScope.launch {

            history.endTimeMilli = System.currentTimeMillis()
            dataSource.insert(history)

        }}
    fun formatTime(seconds: Int, minutes: Int, hours: Int): String {
        return String.format("%02d", hours) + ":" + String.format(
            "%02d",
            minutes
        ) + ":" + String.format(
            "%02d",
            seconds
        )
    }
    fun stopTimer() {
        if (timerTask != null) {
            timerTask!!.cancel()
            time = 0.0
            timerStart = false
            timerValue!!.value = formatTime(0, 0, 0)

        }
    }
    fun pauseTimer(){
        if (timerStart == true) {
            timerStart = false
            timerTask!!.cancel()
}

    }


}
class TimerViewModelFactory(
    private val dataSource: HistoryDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            return TimerViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}