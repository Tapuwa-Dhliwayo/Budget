package com.example.budgettracker.ui.analytics

import com.example.budgettracker.data.model.CategorySpending

data class AnalyticsUiState(
    val isLoading: Boolean = false,
    val categories: List<CategorySpending> = emptyList(),
    val error: String? = null
)
