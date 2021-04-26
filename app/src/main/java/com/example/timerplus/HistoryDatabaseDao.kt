package com.example.timerplus


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface HistoryDatabaseDao {

       @Insert
        suspend fun insert(time: History)


        @Update
        suspend fun update(time: History)

        @Query("SELECT * FROM Bg_Timer")
        fun getAlltime(): LiveData<List<History>>

        @Query("DELETE FROM Bg_Timer")
        suspend fun clear()


        @Query("SELECT * FROM Bg_Timer ORDER BY usedId DESC LIMIT 1")
        suspend fun getThisTime(): History?
    }
