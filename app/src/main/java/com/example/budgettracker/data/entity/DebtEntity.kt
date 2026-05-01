package com.example.budgettracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "debts")
data class DebtEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val debtType: String,
    val startingBalance: Double,
    val currentBalance: Double,
    val interestRate: Double = 0.0,
    val minimumPayment: Double = 0.0,
    val paymentDueDay: Int = 1,
    val isUnderDebtReview: Boolean = false,
    val interestStillApplies: Boolean = true,
    val strategy: String = "HYBRID",
    val notes: String = "",
    val createdDate: String,
    val closedDate: String? = null
)
