package com.example.budgettracker.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "debt_payments",
    foreignKeys = [
        ForeignKey(
            entity = DebtEntity::class,
            parentColumns = ["id"],
            childColumns = ["debtId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["debtId"]), Index(value = ["date"])]
)
data class DebtPaymentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val debtId: Long,
    val amount: Double,
    val date: String,
    val paymentType: String,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
