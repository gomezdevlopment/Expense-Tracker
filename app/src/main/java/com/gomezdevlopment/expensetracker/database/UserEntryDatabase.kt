package com.gomezdevlopment.expensetracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntry::class], version = 1, exportSchema = false)
abstract class UserEntryDatabase: RoomDatabase() {

    abstract fun entryDao(): EntryDao

    companion object{
        @Volatile
        private var INSTANCE: UserEntryDatabase? = null

        fun getDatabase(context: Context): UserEntryDatabase{
            val tempInstance = INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserEntryDatabase::class.java,
                    "user_entries"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}