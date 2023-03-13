package com.jmerilai19.reminderapp.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.time.LocalDateTime
import androidx.lifecycle.asLiveData

class ReminderViewModel(application: Application): AndroidViewModel(application) {

    var allReminders: LiveData<List<Reminder>>
    var allUnseenReminders: LiveData<List<Reminder>>
    var allSeenReminders: LiveData<List<Reminder>>

    var mode: String = "Time"
    var msg: String = ""

    private val repository: ReminderRepository

    init {
        val reminderDao = ReminderDatabase.getDatabase(application).reminderDao()
        repository = ReminderRepository(reminderDao)

        allReminders = repository.getAllReminders()
        allUnseenReminders = repository.getAllUnseenReminders()
        allSeenReminders = repository.getAllSeenReminders()
    }

    fun addReminder(reminder: Reminder){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addReminder(reminder)
        }
    }

    fun updateReminder(reminder: Reminder){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateReminder(reminder)
        }
    }

    fun updateMessageById(id: Int, message: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMessageById(id, message)
        }
    }

    fun updateMessageSeenById(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.markAsSeen(id)
            allReminders = repository.getAllReminders()
            allUnseenReminders = repository.getAllUnseenReminders()
            allSeenReminders = repository.getAllSeenReminders()
        }
    }

    fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteReminder(reminder)
        }
    }

    fun updateSeenLists() {
        viewModelScope.launch(Dispatchers.IO) {
            allUnseenReminders = repository.getAllUnseenReminders()
            allSeenReminders = repository.getAllSeenReminders()
        }
    }

    fun getById(id: Int): Reminder {
        return repository.getById(id)
    }

    fun getRemindersNearLocation(x: Double, y: Double): List<Reminder> {
        return repository.getNearby(x-0.02, x+0.02, y-0.02, y+0.02)
    }
}
