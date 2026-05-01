package com.example.budgettracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.budgettracker.data.entity.WeeklyAllowanceEntity

@Dao
interface WeeklyAllowanceDao {
    @Query("SELECT * FROM weekly_allowance WHERE weekStartDate = :weekStartDate LIMIT 1")
    suspend fun getWeek(weekStartDate: String): WeeklyAllowanceEntity?

    @Query("SELECT * FROM weekly_allowance ORDER BY weekStartDate DESC LIMIT :limit")
    suspend fun getRecentWeeks(limit: Int): List<WeeklyAllowanceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWeek(allowance: WeeklyAllowanceEntity)
}
