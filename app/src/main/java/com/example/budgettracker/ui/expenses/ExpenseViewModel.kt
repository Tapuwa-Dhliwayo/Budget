package com.example.budgettracker.ui.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker.data.entity.ExpenseEntity
import com.example.budgettracker.data.model.ExpenseWithCategory
import com.example.budgettracker.data.repository.ExpenseRepository
import com.example.budgettracker.data.repository.GamificationRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ExpenseUiState(
    val expenses: List<ExpenseWithCategory> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ExpenseViewModel(
    private val expenseRepository: ExpenseRepository,
    private val gamificationRepository: GamificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

    init {
        loadExpenses()
    }

    private fun loadExpenses() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            expenseRepository.observeExpensesWithCategory()
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to load expenses: ${e.message}"
                    )
                }
                .collect { expenses ->
                    _uiState.value = ExpenseUiState(
                        expenses = expenses,
                        isLoading = false
                    )
                }
        }
    }

    /** ✅ SUSPEND = TESTABLE */
    suspend fun addExpense(
        amount: Double,
        description: String,
        date: String,
        categoryId: Long,
        photoPath: String? = null
    ) {
        try {
            val expense = ExpenseEntity(
                amount = amount,
                description = description,
                date = date,
                categoryId = categoryId,
                photoPath = photoPath
            )

            expenseRepository.insertExpense(expense)
            gamificationRepository.updateStreakForNewExpense(date)

        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                error = "Failed to add expense: ${e.message}"
            )
        }
    }

    fun deleteExpense(expense: ExpenseWithCategory) {
        viewModelScope.launch {
            try {
                val expenseEntity = expenseRepository.getExpenseById(expense.expenseId)
                if (expenseEntity != null) {
                    expenseRepository.deleteExpense(expenseEntity)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to delete expense: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
