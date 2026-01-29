package com.example.budgettracker.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker.data.entity.CategoryEntity
import com.example.budgettracker.data.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState(isLoading = true))
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    fun loadCategories() {
        viewModelScope.launch {
            try {
                val categories = repository.getAllCategories()
                _uiState.value = CategoryUiState(categories = categories)
            } catch (e: Exception) {
                _uiState.value =
                    CategoryUiState(error = "Failed to load categories")
            }
        }
    }

    suspend fun addCategory(category: CategoryEntity) {
        repository.insertCategory(category)
        loadCategories()
    }

    suspend fun updateCategory(category: CategoryEntity) {
        repository.updateCategory(category)
        loadCategories()
    }

    suspend fun deleteCategory(id: Long) {
        repository.deleteCategory(id)
        loadCategories()
    }
}
