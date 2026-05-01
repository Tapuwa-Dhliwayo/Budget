package com.example.budgettracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gamification")
data class GamificationEntity(
    @PrimaryKey val id: Int = 1, // Singleton entity
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastLoggedDate: String? = null, // YYYY-MM-DD
    val badgesEarned: String = "", // Comma-separated badge IDs
    val totalExpensesLogged: Int = 0,
    val totalXp: Int = 0
)
