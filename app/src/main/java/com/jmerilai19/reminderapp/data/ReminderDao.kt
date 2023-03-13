package com.jmerilai19.reminderapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import java.time.LocalDateTime

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminder_table ORDER BY reminder_datetime DESC")
    fun getAll(): LiveData<List<Reminder>>

    @Query("SELECT * FROM reminder_table WHERE reminder_seen == false ORDER BY reminder_datetime DESC")
    fun getAllUnseen(): LiveData<List<Reminder>>

    @Query("SELECT * FROM reminder_table WHERE reminder_seen == true ORDER BY reminder_datetime DESC")
    fun getAllSeen(): LiveData<List<Reminder>>

    @Query("SELECT * FROM reminder_table WHERE id == :id")
    fun getById(id: Int): Reminder

    @Query("SELECT * FROM reminder_table WHERE reminder_datetime == :dt")
    fun getByDateTime(dt: String?): Reminder

    @Query("SELECT * FROM reminder_table WHERE location_x BETWEEN :minX AND :maxX AND location_y BETWEEN :minY AND :maxY AND reminder_seen == false")
    fun getRemindersNearLocation(minX: Double, maxX: Double, minY: Double, maxY: Double): List<Reminder>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(reminder: Reminder)

    @Update
    fun update(reminder: Reminder)

    @Query("UPDATE reminder_table SET message=:message WHERE id=:id")
    fun updateMessageById(id: Int, message: String)

    @Query("UPDATE reminder_table SET reminder_seen=true WHERE id=:id")
    fun markAsSeen(id: Int)

    @Delete
    fun delete(reminder: Reminder)

}