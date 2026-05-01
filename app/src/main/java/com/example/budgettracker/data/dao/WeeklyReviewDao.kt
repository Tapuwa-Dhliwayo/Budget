package com.example.budgettracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.budgettracker.data.entity.WeeklyReviewEntity

@Dao
interface WeeklyReviewDao {
    @Query("SELECT * FROM weekly_review WHERE weekStartDate = :weekStartDate LIMIT 1")
    suspend fun getReview(weekStartDate: String): WeeklyReviewEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertReview(review: WeeklyReviewEntity)
}
