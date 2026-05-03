package com.example.budgettracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1, // Single user per account
    val firstName: String,
    val lastName: String,
    val createdDate: String, // ISO date
    val budgetStartDay: Int = 1,
    val themeKey: String = "recovery_arcade"
)
