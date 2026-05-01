package com.example.budgettracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.budgettracker.data.entity.WeeklyCategoryAllowanceEntity

@Dao
interface WeeklyCategoryAllowanceDao {
    @Query("SELECT * FROM weekly_category_allowance WHERE weekStartDate = :weekStartDate")
    suspend fun getWeekLimits(weekStartDate: String): List<WeeklyCategoryAllowanceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLimit(limit: WeeklyCategoryAllowanceEntity)
}
