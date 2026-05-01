package com.example.budgettracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.budgettracker.data.entity.WeeklyRecoveryActionEntity

@Dao
interface WeeklyRecoveryActionDao {
    @Query("SELECT * FROM weekly_recovery_action WHERE weekStartDate = :weekStartDate")
    suspend fun getWeekActions(weekStartDate: String): List<WeeklyRecoveryActionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAction(action: WeeklyRecoveryActionEntity)
}
