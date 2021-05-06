package com.example.timerplus.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Bg_Timer")
data class History(
        @PrimaryKey(autoGenerate = true)
        var usedId: Long = 0L,

        @ColumnInfo(name = "start_time_milli")
        var startTimeMilli: Long = System.currentTimeMillis(),

        @ColumnInfo(name = "end_time_milli")
        var endTimeMilli: Long = System.currentTimeMillis(),

        )
