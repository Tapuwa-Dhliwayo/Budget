package com.example.budgettracker.ui.extraincome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker.data.model.ExtraIncomeAllocationType
import com.example.budgettracker.data.model.ExtraIncomeType
import com.example.budgettracker.data.repository.ExtraIncomeRepository
import com.example.budgettracker.utils.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExtraIncomeViewModel(
    private val repository: ExtraIncomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExtraIncomeUiState(isLoading = true))
    val uiState: StateFlow<ExtraIncomeUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                _uiState.value = ExtraIncomeUiState(
                    summary = repository.getMonthlyImpact(),
                    debtOptions = repository.getDebtOptions(),
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load extra income: ${e.message}"
                )
            }
        }
    }

    fun addIncome(
        source: String,
        incomeType: ExtraIncomeType,
        amount: Double,
        allocationType: ExtraIncomeAllocationType,
        linkedDebtId: Long?,
        notes: String
    ) {
        viewModelScope.launch {
            try {
                repository.addIncome(
                    source = source,
                    incomeType = incomeType,
                    amount = amount,
                    dateReceived = DateUtils.getCurrentDate(),
                    allocationType = allocationType,
                    linkedDebtId = linkedDebtId,
                    notes = notes
                )
                load()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to add extra income: ${e.message}")
            }
        }
    }
}
