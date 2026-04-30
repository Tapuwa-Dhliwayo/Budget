package com.example.budgettracker.ui.analytics

import com.example.budgettracker.data.model.CategorySpending

data class AnalyticsUiState(
    val isLoading: Boolean = false,
    val categories: List<CategorySpending> = emptyList(),
    val totalSpent: Double = 0.0,
    val totalBudget: Double = 0.0,
    val overBudgetCount: Int = 0,
    val topCategoryName: String? = null,
    val topCategoryAmount: Double = 0.0,
    val error: String? = null
)
