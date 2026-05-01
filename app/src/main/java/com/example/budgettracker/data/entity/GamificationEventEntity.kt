package com.example.budgettracker.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "gamification_events",
    indices = [
        Index(value = ["eventType"]),
        Index(value = ["occurredDate"])
    ]
)
data class GamificationEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val eventType: String,
    val xpEarned: Int,
    val occurredDate: String,
    val message: String,
    val relatedId: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)
