package com.example.budgettracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monthly_budget")
data class MonthlyBudgetEntity(
    @PrimaryKey val monthId: String, //2026-01
    val startDate: String, // ISO date temp
    val startingFunds: Double
)
