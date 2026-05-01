package com.example.budgettracker.ui.goals

import com.example.budgettracker.data.model.GoalSummary

data class GoalsUiState(
    val summary: GoalSummary? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
