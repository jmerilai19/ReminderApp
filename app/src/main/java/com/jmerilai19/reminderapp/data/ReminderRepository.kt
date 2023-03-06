package com.jmerilai19.reminderapp.data

import androidx.lifecycle.LiveData
import java.time.LocalDateTime

class ReminderRepository(private val reminderDao: ReminderDao) {

    fun getAllReminders(): LiveData<List<Reminder>> {
        return reminderDao.getAll()
    }

    fun getAllUnseenReminders(): LiveData<List<Reminder>> {
        return reminderDao.getAllUnseen()
    }

    fun getAllSeenReminders(): LiveData<List<Reminder>> {
        return reminderDao.getAllSeen()
    }

    fun addReminder(reminder: Reminder){
        reminderDao.insert(reminder)
    }

    fun updateReminder(reminder: Reminder){
        reminderDao.update(reminder)
    }

    fun updateMessageById(id: Int, message: String){
        reminderDao.updateMessageById(id, message)
    }

    fun deleteReminder(reminder: Reminder){
        reminderDao.delete(reminder)
    }

    fun getById(id: Int): Reminder {
        return reminderDao.getById(id)
    }

    fun getByDateTime(dt: String): Reminder {
        return reminderDao.getByDateTime(dt)
    }

}