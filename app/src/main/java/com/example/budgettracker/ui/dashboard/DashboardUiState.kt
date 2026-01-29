package com.example.budgettracker.ui.dashboard

import com.example.budgettracker.data.model.CategorySpending
import com.example.budgettracker.data.model.Badge

data class DashboardUiState(
    val isLoading: Boolean = false,
    val monthId: String = "",
    val totalBudget: Double = 0.0,
    val totalSpent: Double = 0.0,
    val remaining: Double = 0.0,
    val percentageUsed: Double = 0.0,
    val isOverBudget: Boolean = false,
    val topCategories: List<CategorySpending> = emptyList(),
    val streak: Int = 0,
    val badges: List<Badge> = emptyList(),
    val error: String? = null
)
