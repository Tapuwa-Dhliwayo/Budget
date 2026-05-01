package com.example.budgettracker.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "extra_income",
    indices = [
        Index(value = ["dateReceived"]),
        Index(value = ["allocationType"]),
        Index(value = ["linkedDebtId"])
    ]
)
data class ExtraIncomeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val source: String,
    val incomeType: String,
    val amount: Double,
    val dateReceived: String,
    val allocationType: String,
    val linkedDebtId: Long? = null,
    val linkedGoalId: Long? = null,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
