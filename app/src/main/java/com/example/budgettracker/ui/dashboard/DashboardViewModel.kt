package com.example.budgettracker.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker.data.model.CategorySpending
import com.example.budgettracker.data.model.MonthlyOverview
import com.example.budgettracker.data.repository.AnalyticsRepository
import com.example.budgettracker.data.repository.BudgetRepository
import com.example.budgettracker.utils.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DashboardUiState(
    val isLoading: Boolean = true,
    val monthlyOverview: MonthlyOverview? = null,
    val categorySpending: List<CategorySpending> = emptyList(),
    val error: String? = null
)

class DashboardViewModel(
    private val budgetRepository: BudgetRepository,
    private val analyticsRepository: AnalyticsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard(monthId: String = DateUtils.getCurrentMonthId()) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // Ensure month exists
                budgetRepository.loadOrCreateMonth(monthId, 0.0)

                // Load analytics
                val overview = analyticsRepository.getMonthlyOverview(monthId)
                val spending = analyticsRepository.getCategorySpending(monthId)

                _uiState.value = DashboardUiState(
                    isLoading = false,
                    monthlyOverview = overview,
                    categorySpending = spending
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load dashboard: ${e.message}"
                )
            }
        }
    }

    fun refreshDashboard() {
        loadDashboard()
    }
}
