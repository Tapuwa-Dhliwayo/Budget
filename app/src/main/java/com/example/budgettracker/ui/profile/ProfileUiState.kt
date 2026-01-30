package com.example.budgettracker.ui.profile

data class ProfileUiState(
    val isLoading: Boolean = false,
    val firstName: String = "",
    val lastName: String = "",
    val fullName: String = "",
    val error: String? = null,
    val saveSuccess: Boolean = false
)
