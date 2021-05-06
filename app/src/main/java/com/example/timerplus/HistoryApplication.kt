package com.example.timerplus

import android.app.Application
import com.example.timerplus.database.HistoryDatabase
import com.example.timerplus.database.HistoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class HistoryApplication : Application(){
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { HistoryDatabase.getInstance(this) }
    val repository by lazy { HistoryRepository( database.historyDatabaseDao) }
}