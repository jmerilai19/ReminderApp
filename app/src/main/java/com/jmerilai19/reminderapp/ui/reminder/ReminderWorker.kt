package com.jmerilai19.reminderapp.ui.reminder

import android.content.Context
import android.util.Log
import androidx.room.Room.databaseBuilder
import androidx.work.*
import com.jmerilai19.reminderapp.data.Reminder
import com.jmerilai19.reminderapp.data.ReminderDatabase
import com.jmerilai19.reminderapp.data.ReminderRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private val service = ReminderNotificationService(context)

    override suspend fun doWork(): Result {
        if (inputData.getBoolean("notis", false)) {
            service.showNotification(inputData.getString("title"))
        }

        var r: Reminder = ReminderDatabase.getDatabase(context = applicationContext).reminderDao().getByDateTime(inputData.getString("dt"))



        if (r.type == 3) { // If daily, set to happen the next day
            r.reminder_datetime.plusDays(1)
        } else { // if not repeating, set as seen
            r.reminder_seen = true
        }

        ReminderDatabase.getDatabase(context = applicationContext).reminderDao().update(r)

        // Indicate success
        return Result.success()
    }
}