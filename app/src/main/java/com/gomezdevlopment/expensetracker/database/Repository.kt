package com.gomezdevlopment.expensetracker.database

import androidx.lifecycle.LiveData

class Repository(private val entryDao: EntryDao) {
    val userEntries: LiveData<List<UserEntry>> = entryDao.getUserEntries()
    val incomeEntries: LiveData<List<UserEntry>> = entryDao.getIncomeEntries()
    val expenseEntries: LiveData<List<UserEntry>> = entryDao.getExpenseEntries()
    val distinctDates: LiveData<List<String>> = entryDao.getDistinctDates()

    suspend fun addEntry(entry: UserEntry){
        entryDao.addUserEntry(entry)
    }

    suspend fun deleteEntry(entry: UserEntry){
        entryDao.deleteEntry(entry)
    }

    fun getIncomeEntriesByDate(date: String): LiveData<List<UserEntry>>{
        return entryDao.getIncomeEntriesByDate(date)
    }

    fun getExpenseEntriesByDate(date: String): LiveData<List<UserEntry>>{
        return entryDao.getExpenseEntriesByDate(date)
    }

    fun getEntriesByDate(date: String): LiveData<List<UserEntry>>{
        return entryDao.getEntriesByDate(date)
    }
}