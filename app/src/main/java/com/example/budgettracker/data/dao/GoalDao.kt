package com.example.budgettracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.budgettracker.data.entity.GoalEntity

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals ORDER BY status = 'ACTIVE' DESC, priority, targetDate IS NULL, targetDate, updatedAt DESC")
    suspend fun getAllGoals(): List<GoalEntity>

    @Query("SELECT * FROM goals WHERE status = 'ACTIVE' ORDER BY priority, targetDate IS NULL, targetDate")
    suspend fun getActiveGoals(): List<GoalEntity>

    @Query("SELECT * FROM goals WHERE id = :id LIMIT 1")
    suspend fun getGoalById(id: Long): GoalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalEntity): Long

    @Update
    suspend fun updateGoal(goal: GoalEntity)
}
