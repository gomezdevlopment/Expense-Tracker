package com.gomezdevlopment.expensetracker.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface EntryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUserEntry(userEntry: UserEntry)

    @Delete
    suspend fun deleteEntry(entry: UserEntry)

    @Query("SELECT * FROM user_entries")
    fun getUserEntries(): LiveData<List<UserEntry>>

    @Query("SELECT * FROM user_entries WHERE entryType = 'income'")
    fun getIncomeEntries(): LiveData<List<UserEntry>>

    @Query("SELECT * FROM user_entries WHERE entryType = 'expense'")
    fun getExpenseEntries(): LiveData<List<UserEntry>>

}