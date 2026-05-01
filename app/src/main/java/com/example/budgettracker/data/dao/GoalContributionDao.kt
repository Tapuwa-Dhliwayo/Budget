package com.example.budgettracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.budgettracker.data.entity.GoalContributionEntity

@Dao
interface GoalContributionDao {
    @Query("SELECT * FROM goal_contributions WHERE goalId = :goalId ORDER BY contributionDate DESC, id DESC")
    suspend fun getContributionsForGoal(goalId: Long): List<GoalContributionEntity>

    @Query("SELECT * FROM goal_contributions ORDER BY contributionDate DESC, id DESC LIMIT :limit")
    suspend fun getRecentContributions(limit: Int): List<GoalContributionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContribution(contribution: GoalContributionEntity): Long
}
