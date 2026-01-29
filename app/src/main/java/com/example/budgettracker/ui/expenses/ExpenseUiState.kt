package com.example.budgettracker.ui.expenses

import com.example.budgettracker.data.model.ExpenseWithCategory

data class ExpenseUiState(
    val expenses: List<ExpenseWithCategory> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
