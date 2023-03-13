package com.jmerilai19.reminderapp.ui.reminder

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room.databaseBuilder
import androidx.work.*
import com.google.android.gms.maps.model.LatLng
import com.jmerilai19.reminderapp.data.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private val service = ReminderNotificationService(context)

    override suspend fun doWork(): Result {

        Log.d("work", "w")

        var r: Reminder = ReminderDatabase.getDatabase(context = applicationContext).reminderDao().getByDateTime(inputData.getString("dt"))

        val sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val manualLocationLat = sharedPreferences.getFloat("MANUAL_LOCATION_LAT", 65.06f).toDouble()
        val manualLocationLng = sharedPreferences.getFloat("MANUAL_LOCATION_LNG", 25.47f).toDouble()

        if (inputData.getBoolean("notis", false)) {
            if (r.type == 2) {
                if (manualLocationLat >= r.location_x-0.02 && manualLocationLat <= r.location_x+0.02 && manualLocationLng >= r.location_y-0.02 && manualLocationLng <= r.location_y+0.02) {
                    service.showNotification(inputData.getString("title"))
                }
            } else {
                service.showNotification(inputData.getString("title"))
            }

        }

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