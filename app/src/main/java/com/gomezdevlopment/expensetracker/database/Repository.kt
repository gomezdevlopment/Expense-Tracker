package com.gomezdevlopment.expensetracker.database

import androidx.lifecycle.LiveData

class Repository(private val entryDao: EntryDao) {
    val userEntries: LiveData<List<UserEntry>> = entryDao.getUserEntries()
    val incomeEntries: LiveData<List<UserEntry>> = entryDao.getIncomeEntries()
    val expenseEntries: LiveData<List<UserEntry>> = entryDao.getExpenseEntries()

    suspend fun addEntry(entry: UserEntry){
        entryDao.addUserEntry(entry)
    }
}