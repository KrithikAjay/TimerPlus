package com.example.timerplus

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.timerplus.database.History


@BindingAdapter("historyText")
fun TextView.setHistoryText(history : History){
    history?.let {
        text = formatHistory(history.startTimeMilli,history.endTimeMilli,context.resources)
    }
}