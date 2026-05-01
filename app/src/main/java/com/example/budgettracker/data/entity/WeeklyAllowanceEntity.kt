package com.example.budgettracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weekly_allowance")
data class WeeklyAllowanceEntity(
    @PrimaryKey val weekStartDate: String,
    val weekEndDate: String,
    val allowanceAmount: Double,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
