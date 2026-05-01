package com.example.budgettracker.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "goals",
    indices = [
        Index(value = ["status"]),
        Index(value = ["targetDate"]),
        Index(value = ["goalType"])
    ]
)
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val goalType: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val targetDate: String?,
    val monthlyContributionTarget: Double,
    val priority: String,
    val healthClassification: String,
    val status: String,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val completedAt: String? = null
)
