package com.example.budgettracker.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker.data.entity.MonthlyBudgetEntity
import com.example.budgettracker.data.repository.BudgetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: BudgetRepository
) : ViewModel() {

    private val _monthState =
        MutableStateFlow<MonthlyBudgetEntity?>(null)

    val monthState: StateFlow<MonthlyBudgetEntity?>
        get() = _monthState

    fun loadMonth(
        monthId: String,
        startingFunds: Double
    ) {
        viewModelScope.launch {
            val month = repository.loadOrCreateMonth(
                monthId = monthId,
                startingFunds = startingFunds
            )
            _monthState.value = month
        }
    }
}