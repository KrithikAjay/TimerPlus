package com.example.timerplus.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class HistoryRepository(private val historyDatabaseDao: HistoryDatabaseDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val getAllTime: LiveData<List<History>> =  historyDatabaseDao.getAlltime()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(history : History) {
       historyDatabaseDao.insert(history)
    }
    suspend fun update(history: History){
        historyDatabaseDao.update(history)
    }
    suspend fun getThisTime() : History?   {
      return  historyDatabaseDao.getThisTime()
    }
    suspend fun clear (){
        historyDatabaseDao.clear()
    }
}