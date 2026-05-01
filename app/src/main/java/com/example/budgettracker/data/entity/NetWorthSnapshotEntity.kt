package com.example.budgettracker.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "net_worth_snapshots",
    indices = [Index(value = ["snapshotDate"])]
)
data class NetWorthSnapshotEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val snapshotDate: String,
    val totalAssets: Double,
    val totalDebts: Double,
    val netWorth: Double,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
