package com.example.budgettracker.ui.gamification

import com.example.budgettracker.data.model.GamificationStatus

data class GamificationUiState(
    val status: GamificationStatus? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
