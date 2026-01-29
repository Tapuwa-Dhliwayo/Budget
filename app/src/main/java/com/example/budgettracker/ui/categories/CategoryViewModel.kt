package com.example.budgettracker.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker.data.entity.CategoryEntity
import com.example.budgettracker.data.repository.CategoryRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CategoryUiState(
    val categories: List<CategoryEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class CategoryViewModel(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            repository.observeAllCategories()
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to load categories: ${e.message}"
                    )
                }
                .collect { categories ->
                    _uiState.value = CategoryUiState(
                        categories = categories,
                        isLoading = false
                    )
                }
        }
    }

    fun addCategory(
        name: String,
        color: Int,
        icon: String,
        budgetLimit: Double
    ) {
        viewModelScope.launch {
            try {
                val category = CategoryEntity(
                    name = name,
                    color = color,
                    icon = icon,
                    budgetLimit = budgetLimit
                )
                repository.insertCategory(category)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to add category: ${e.message}"
                )
            }
        }
    }

    fun updateCategory(category: CategoryEntity) {
        viewModelScope.launch {
            try {
                repository.updateCategory(category)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update category: ${e.message}"
                )
            }
        }
    }

    fun deleteCategory(id: Long) {
        viewModelScope.launch {
            try {
                repository.deleteCategory(id)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to delete category: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
