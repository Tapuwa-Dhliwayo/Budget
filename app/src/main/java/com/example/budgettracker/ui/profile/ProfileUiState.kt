package com.example.budgettracker.ui.profile

data class ProfileUiState(
    val isLoading: Boolean = false,
    val firstName: String = "",
    val lastName: String = "",
    val fullName: String = "",
    val budgetStartDay: Int = 1,
    val themeKey: String = "recovery_arcade",
    val error: String? = null,
    val saveSuccess: Boolean = false
)
