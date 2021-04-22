package com.example.timerplus

import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.graphics.drawable.VectorDrawable
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog;

import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import java.util.Timer;
import java.util.TimerTask;

class MainActivity : AppCompatActivity() {
    //enum class for describe the state of te timer
    var timerStart : Boolean = false
    var time = 0.0
    lateinit var  timerTask : TimerTask
    var timerText: TextView? = null
    var timer: Timer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setIcon(R.drawable.ic_timer)
        supportActionBar?.title = "  TimerPlus"

        val startButton = findViewById<FloatingActionButton>(R.id.fab_start)
        val stopButton = findViewById<FloatingActionButton>(R.id.fab_stop)
        val pauseButton = findViewById<FloatingActionButton>(R.id.fab_pause)
         timerText = findViewById<TextView>(R.id.timer_text)

        timer = Timer()
        val progressBar =  findViewById<ProgressBar>(R.id.progress_circular)
        startButton.setOnClickListener{
            if (timerStart == false) {
                timerStart = true
                progressBar.isVisible = true

                startTimer()
                Toast.makeText(this,"Started!!",Toast.LENGTH_SHORT).show()


            }


        }
        pauseButton.setOnClickListener{
            if(timerStart == true) {
                timerStart = false

                timerTask!!.cancel()
                Toast.makeText(this,"Paused",Toast.LENGTH_SHORT).show()

            }

            progressBar.isVisible = false


        }
        stopButton.setOnClickListener {
            val resetAlert = AlertDialog.Builder(this)
            resetAlert.setTitle("Reset Timer")
            resetAlert.setMessage("Are you sure you want to reset the timer?")
            resetAlert.setPositiveButton("Reset") { dialogInterface, i ->
                if (timerTask != null) {
                    progressBar.isVisible= false
                    timerTask!!.cancel()



                    time = 0.0
                    timerStart = false
                    timerText!!.text = formatTime(0, 0, 0)
                    Toast.makeText(this,"Reseted",Toast.LENGTH_SHORT).show()
                }
            }
            resetAlert.setNeutralButton("Cancel") { dialogInterface, i ->
                //do nothing
            }
            resetAlert.show()

        }






    }



//
////    override fun onCreateOptionsMenu(menu: Menu): Boolean {
////        // Inflate the menu; this adds items to the action bar if it is present.
////        menuInflater.inflate(R.menu.menu_main, menu)
////        return true
////    }
////
////    override fun onOptionsItemSelected(item: MenuItem): Boolean {
////        // Handle action bar item clicks here. The action bar will
////        // automatically handle clicks on the Home/Up button, so long
////        // as you specify a parent activity in AndroidManifest.xml.
////        return when (item.itemId) {
////            R.id.action_settings -> true
////            else -> super.onOptionsItemSelected(item)
////        }
//    }

private fun startTimer() {
    timerTask = object : TimerTask() {
        override fun run() {
            runOnUiThread {
                time++
                timerText!!.text = getTimerText()
            }
        }
    }
    timer!!.scheduleAtFixedRate(timerTask, 0, 1000)
}

    private fun getTimerText(): String {
        val rounded = Math.round(time).toInt()
        val seconds = rounded % 86400 % 3600 % 60
        val minutes = rounded % 86400 % 3600 / 60
        val hours = rounded % 86400 / 3600
        return formatTime(seconds, minutes, hours)
    }

    private fun formatTime(seconds: Int, minutes: Int, hours: Int): String {
        return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format(
                "%02d",
                seconds
        )
    }
}