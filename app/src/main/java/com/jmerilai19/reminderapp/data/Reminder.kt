package com.jmerilai19.reminderapp.data

import androidx.room.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Entity(tableName = "reminder_table")
@TypeConverters(LocalDateTimeConverter::class)
data class Reminder (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "message") var message: String = "",
    val type: Int = 0, // 0 = one time, 1 = location, 2 = both, 3 = time daily, 4 = time weekly
    var location: String = "",
    @ColumnInfo(name = "location_x") var location_x: Double = 0.0,
    @ColumnInfo(name = "location_y") var location_y: Double = 0.0,
    var weekday: String = "",
    @ColumnInfo(name = "reminder_datetime") var reminder_datetime: LocalDateTime = LocalDateTime.MIN,
    val creation_time: LocalDateTime = LocalDateTime.MIN,
    val creator_id: String = "",
    val notification_on: Boolean = false,
    @ColumnInfo(name = "reminder_seen") var reminder_seen: Boolean = false
)

class LocalDateTimeConverter {
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }
}
