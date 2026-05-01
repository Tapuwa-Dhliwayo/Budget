package com.example.budgettracker.ui.debt

import com.example.budgettracker.data.model.DebtBattleSummary
import com.example.budgettracker.data.model.DebtBoss

data class DebtBossUiState(
    val debts: List<DebtBoss> = emptyList(),
    val battleSummary: DebtBattleSummary? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
