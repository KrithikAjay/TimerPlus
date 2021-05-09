package com.example.timerplus

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.timerplus.database.History
import com.example.timerplus.database.HistoryDatabaseDao
import kotlinx.coroutines.launch
import java.util.*

class TimerViewModel(val dataSource: HistoryDatabaseDao, application: Application) : ViewModel() {

    //Required Variables
    var history = History()
    var timerStart: Boolean = false
    var time = 0.0
    var timer: Timer? = null
    var timerTask : TimerTask? =  null

    //Required MutableLiveData
    val _timerValue = MutableLiveData<String>()
    val timerValue : LiveData<String>
    get() = _timerValue

    val _progressBarVisible = MutableLiveData<Boolean>()
    val progressBarVisible :LiveData<Boolean>
    get() = _progressBarVisible

    val _showToast = MutableLiveData<Int>()
    val showToast :LiveData<Int>
    get() = _showToast
    fun doneShowToast(){
        _showToast.value = -1
    }


    //Intialize values
    init {
        timer = Timer()
        _timerValue.value = "00:00:00"
        _progressBarVisible.value = false

    }


    //Start Timer Function
   fun startTimer() {
       if (!timerStart) {
           timerStart = true
           onStartTracking()

           val activity = Activity()
//       val handler  = Handler(Looper.getMainLooper())

           timerTask = object : TimerTask() {
               override fun run() {
                   activity.runOnUiThread {
                       time++
                       _timerValue!!.value = getTimerText()
                   }
               }
           }
           _progressBarVisible.value = true
           _showToast.value = 1
           timer!!.scheduleAtFixedRate(timerTask, 0, 1000)
       }
    }

    //Function for changing time into get Timer String
    private fun getTimerText(): String {
        val rounded = Math.round(time).toInt()
        val seconds = rounded % 86400 % 3600 % 60
        val minutes = rounded % 86400 % 3600 / 60
        val hours = rounded % 86400 / 3600
        return formatTime(seconds, minutes, hours)
    }


    //function for get Format Timer
    private fun formatTime(seconds: Int, minutes: Int, hours: Int): String {
        return String.format("%02d", hours) + ":" + String.format(
                "%02d",
                minutes
        ) + ":" + String.format(
                "%02d",
                seconds
        )
    }

    // function for insert starting time of the application in database
    private fun onStartTracking() {
        viewModelScope.launch {
            history.startTimeMilli = System.currentTimeMillis()


        }
    }

    // function for insert ending time of the application in database
   private fun onStopTracking() {
        viewModelScope.launch {
            history.endTimeMilli = System.currentTimeMillis()
            dataSource.insert(history)

        }}



    //Reset Timer Function
    fun resetTimer() {

        if (timerTask != null) {
            timerTask!!.cancel()

           onStopTracking()

            time = 0.0
            timerStart = false
            _timerValue!!.value = formatTime(0, 0, 0)
            _progressBarVisible.value = false


        }
    }

    //Pause Timer Function
    fun pauseTimer(){
        if (timerStart) {
            timerStart = false
            timerTask!!.cancel()
            _progressBarVisible.value = false
            _showToast.value = 0
}
    }


}

