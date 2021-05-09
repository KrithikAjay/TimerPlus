package com.example.timerplus


import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import androidx.core.text.HtmlCompat
import com.example.timerplus.database.History

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * These functions create a formatted string that can be set in a TextView.
 */
private val ONE_MINUTE_MILLIS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)
private val ONE_HOUR_MILLIS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

/**
 * Convert a duration to a formatted string for display.
 *
 * Examples:
 *
 * 6 seconds on Wednesday
 * 2 minutes on Monday
 * 40 hours on Thursday
 *
 * @param startTimeMilli the start of the interval
 * @param endTimeMilli the end of the interval
 * @param res resources used to load formatted strings
 */
fun convertDurationToFormatted(startTimeMilli: Long, endTimeMilli: Long, res: Resources): String {
    val durationMilli = endTimeMilli - startTimeMilli
    val weekdayString = SimpleDateFormat("EEEE", Locale.getDefault()).format(startTimeMilli)

    return when {
        durationMilli < ONE_MINUTE_MILLIS -> {
            val seconds = TimeUnit.SECONDS.convert(durationMilli, TimeUnit.MILLISECONDS)
            res.getString(R.string.seconds_length, seconds, weekdayString)
        }
        durationMilli < ONE_HOUR_MILLIS -> {
            val minutes = TimeUnit.MINUTES.convert(durationMilli, TimeUnit.MILLISECONDS)
            res.getString(R.string.minutes_length, minutes, weekdayString)
        }
        else -> {
            val hours = TimeUnit.HOURS.convert(durationMilli, TimeUnit.MILLISECONDS)
            res.getString(R.string.hours_length, hours, weekdayString)
        }
    }
}

/**
 * Returns a string representing the numeric quality rating.
 */



/**
 * Take the Long milliseconds returned by the system and stored in Room,
 * and convert it to a nicely formatted string for display.
 *
 * EEEE - Display the long letter version of the weekday
 * MMM - Display the letter abbreviation of the nmotny
 * dd-yyyy - day in month and full year numerically
 * HH:mm - Hours and minutes in 24hr format
 */
@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm")
        .format(systemTime).toString()
}

/**
 * Takes a list of SleepNights and converts and formats it into one string for display.
 *
 * For display in a TextView, we have to supply one string, and styles are per TextView, not
 * applicable per word. So, we build a formatted string using HTML. This is handy, but we will
 * learn a better way of displaying this data in a future lesson.
 *

 * @return  Spanned - An interface for text that has formatting attached to it.
 *           See: https://developer.android.com/reference/android/text/Spanned
 */

//fun formatTimer(time: List<History>, resources: Resources): Spanned {
//    val sb = StringBuilder()
//    sb.apply {
//        append(resources.getString(R.string.title))
//        time.forEach {
//            append("<br>")
//            append(resources.getString(R.string.start_time))
//            append("\t${convertLongToDateString(it.startTimeMilli)}<br>")
//            if (it.endTimeMilli != it.startTimeMilli) {
//                append(resources.getString(R.string.end_time))
//                append("\t${convertLongToDateString(it.endTimeMilli)}<br>")
//                append(resources.getString(R.string.hours_slept))
//                // Hours
//                append("\t ${it.endTimeMilli.minus(it.startTimeMilli) / 1000 / 60 / 60}:")
//                // Minutes
//                append("${it.endTimeMilli.minus(it.startTimeMilli) / 1000 / 60}:")
//                // Seconds
//                append("${it.endTimeMilli.minus(it.startTimeMilli) / 1000}<br><br>")
//            }
//        }
//    }
//    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//        Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
//    } else {
//        HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
//    }
//}
fun formatHistory(startTimeMilli: Long, endTimeMilli: Long, res: Resources): Spanned{

    val sb = StringBuilder()
    sb.apply {
//        append(res.getString(R.string.title))
        append("<br>")
//
        append(res.getString(R.string.start_time))
        append("\t${convertLongToDateString(startTimeMilli)}<br>")
        if (endTimeMilli != startTimeMilli) {
            append(res.getString(R.string.end_time))
            append("\t${convertLongToDateString(endTimeMilli)}<br>")
            append(res.getString(R.string.hours_slept))
            // Hours
            append("\t ${endTimeMilli.minus(startTimeMilli) / 1000 / 60 / 60}:")
            // Minutes
            append("${endTimeMilli.minus(startTimeMilli) / 1000 / 60}:")
            // Seconds
            append("${endTimeMilli.minus(startTimeMilli) / 1000}")



        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
        } else {
            HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        }


    }}

