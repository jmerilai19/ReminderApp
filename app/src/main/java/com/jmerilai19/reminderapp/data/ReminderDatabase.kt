package com.jmerilai19.reminderapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Reminder::class], version = 30, exportSchema = false)
abstract class ReminderDatabase: RoomDatabase() {

    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var INSTANCE: ReminderDatabase? = null

        fun getDatabase(context: Context): ReminderDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReminderDatabase::class.java,
                    "reminder_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}