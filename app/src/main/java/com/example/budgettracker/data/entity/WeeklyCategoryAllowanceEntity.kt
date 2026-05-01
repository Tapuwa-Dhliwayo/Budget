package com.example.budgettracker.data.entity

import androidx.room.Entity

@Entity(
    tableName = "weekly_category_allowance",
    primaryKeys = ["weekStartDate", "categoryId"]
)
data class WeeklyCategoryAllowanceEntity(
    val weekStartDate: String,
    val weekEndDate: String,
    val categoryId: Long,
    val limitAmount: Double,
    val updatedAt: Long = System.currentTimeMillis()
)
