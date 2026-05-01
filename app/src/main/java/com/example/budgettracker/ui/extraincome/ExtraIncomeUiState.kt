package com.example.budgettracker.ui.extraincome

import com.example.budgettracker.data.model.ExtraIncomeImpactSummary

data class ExtraIncomeUiState(
    val summary: ExtraIncomeImpactSummary? = null,
    val debtOptions: List<Pair<Long, String>> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
