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

    @Query("SELECT * FROM user_entries WHERE SUBSTR(date, 0, 8) = :date")
    fun getEntriesByDate(date: String): LiveData<List<UserEntry>>

    @Query("SELECT * FROM user_entries WHERE SUBSTR(date, 0, 8) = :date AND entryType ='income'")
    fun getIncomeEntriesByDate(date: String): LiveData<List<UserEntry>>

    @Query("SELECT * FROM user_entries WHERE SUBSTR(date, 0, 8) = :date AND entryType ='expense'" )
    fun getExpenseEntriesByDate(date: String): LiveData<List<UserEntry>>

    @Query("SELECT DISTINCT SUBSTR(date, 0, 8) FROM user_entries")
    fun getDistinctDates(): LiveData<List<String>>

}