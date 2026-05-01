package com.example.budgettracker.data.entity

import androidx.room.Entity

@Entity(
    tableName = "weekly_recovery_action",
    primaryKeys = ["weekStartDate", "actionText"]
)
data class WeeklyRecoveryActionEntity(
    val weekStartDate: String,
    val weekEndDate: String,
    val actionText: String,
    val isCompleted: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)
