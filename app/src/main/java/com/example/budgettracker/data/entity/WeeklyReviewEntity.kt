package com.example.budgettracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weekly_review")
data class WeeklyReviewEntity(
    @PrimaryKey val weekStartDate: String,
    val weekEndDate: String,
    val wentWell: String,
    val challenge: String,
    val nextWeekAdjustment: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
