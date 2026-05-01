package com.example.budgettracker.ui.debt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker.data.model.DebtPaymentType
import com.example.budgettracker.data.model.DebtStrategy
import com.example.budgettracker.data.model.DebtType
import com.example.budgettracker.data.repository.DebtRepository
import com.example.budgettracker.utils.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DebtBossViewModel(
    private val repository: DebtRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DebtBossUiState(isLoading = true))
    val uiState: StateFlow<DebtBossUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                _uiState.value = DebtBossUiState(
                    debts = repository.getDebtBosses(),
                    battleSummary = repository.getMonthlyBattleSummary(),
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load debt bosses: ${e.message}"
                )
            }
        }
    }

    fun addDebt(
        name: String,
        debtType: DebtType,
        startingBalance: Double,
        interestRate: Double,
        minimumPayment: Double,
        paymentDueDay: Int,
        isUnderDebtReview: Boolean,
        interestStillApplies: Boolean,
        strategy: DebtStrategy,
        notes: String
    ) {
        viewModelScope.launch {
            try {
                repository.addDebt(
                    name = name,
                    debtType = debtType,
                    startingBalance = startingBalance,
                    interestRate = interestRate,
                    minimumPayment = minimumPayment,
                    paymentDueDay = paymentDueDay,
                    isUnderDebtReview = isUnderDebtReview,
                    interestStillApplies = interestStillApplies,
                    strategy = strategy,
                    notes = notes
                )
                load()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to add debt: ${e.message}")
            }
        }
    }

    fun recordPayment(
        debtId: Long,
        amount: Double,
        paymentType: DebtPaymentType,
        notes: String
    ) {
        viewModelScope.launch {
            try {
                repository.recordPayment(
                    debtId = debtId,
                    amount = amount,
                    date = DateUtils.getCurrentDate(),
                    paymentType = paymentType,
                    notes = notes
                )
                load()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to record payment: ${e.message}")
            }
        }
    }
}
