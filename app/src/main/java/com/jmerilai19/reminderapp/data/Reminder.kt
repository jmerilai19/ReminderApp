package com.jmerilai19.reminderapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder_table")
data class Reminder (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val message: String = "",
    val location: String = "",
    val location_x: String = "",
    val location_y: String = "",
    val reminder_time: String = "",
    val creation_time: String = "",
    val creator_id: String = "",
    val reminder_seen: Boolean = false
)