package com.jmerilai19.reminderapp.data

import androidx.lifecycle.LiveData

class ReminderRepository(private val reminderDao: ReminderDao) {

    fun getAllReminders(): LiveData<List<Reminder>> {
        return reminderDao.getAll()
    }

    fun addReminder(reminder: Reminder){
        reminderDao.insert(reminder)
    }

    fun updateReminder(reminder: Reminder){
        reminderDao.update(reminder)
    }

    fun deleteReminder(reminder: Reminder){
        reminderDao.delete(reminder)
    }

}