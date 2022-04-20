package com.gomezdevlopment.expensetracker.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "user_entries")
data class UserEntry(
    @PrimaryKey (autoGenerate = true)
    val id: Int,
    val title: String,
    val value: Float,
    val date: String
)

