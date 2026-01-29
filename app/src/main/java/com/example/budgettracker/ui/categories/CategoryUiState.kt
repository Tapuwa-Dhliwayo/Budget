package com.example.budgettracker.ui.categories

import com.example.budgettracker.data.entity.CategoryEntity

data class CategoryUiState(
    val isLoading: Boolean = false,
    val categories: List<CategoryEntity> = emptyList(),
    val error: String? = null
)
