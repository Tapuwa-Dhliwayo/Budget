package com.example.budgettracker.ui.networth

import com.example.budgettracker.data.model.NetWorthSummary

data class NetWorthUiState(
    val summary: NetWorthSummary? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
