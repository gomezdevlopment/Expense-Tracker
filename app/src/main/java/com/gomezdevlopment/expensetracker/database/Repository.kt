package com.gomezdevlopment.expensetracker.database

import androidx.lifecycle.LiveData

class Repository(private val entryDao: EntryDao) {
    val userEntries: LiveData<List<UserEntry>> = entryDao.getUserEntries()

    suspend fun addEntry(entry: UserEntry){
        entryDao.addUserEntry(entry)
    }
}