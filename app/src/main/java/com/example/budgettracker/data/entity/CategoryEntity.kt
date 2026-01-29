package com.example.budgettracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val color: Int, // Color resource for visual distinction
    val icon: String, // Emoji or icon identifier
    val budgetLimit: Double = 0.0, // Monthly budget limit for this category
    val isActive: Boolean = true // Soft delete flag
)
