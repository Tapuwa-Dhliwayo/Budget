package com.example.budgettracker.ui.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker.data.repository.AnalyticsRepository
import com.example.budgettracker.data.repository.BudgetRepository
import com.example.budgettracker.data.repository.GamificationRepository
import com.example.budgettracker.utils.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val budgetRepository: BudgetRepository,
    private val analyticsRepository: AnalyticsRepository,
    private val gamificationRepository: GamificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadDashboard(monthId: String = DateUtils.getCurrentMonthId()) {
        viewModelScope.launch {
            _uiState.value = DashboardUiState(isLoading = true, monthId = monthId)

            try {
                // Ensure month exists
                budgetRepository.loadOrCreateMonth(monthId, 0.0)

                // Analytics
                val overview = analyticsRepository.getMonthlyOverview(monthId)
                val topCategories =
                    analyticsRepository.getCategorySpending(monthId).take(3)

                // Gamification
                val gamification = gamificationRepository.getGamificationStatus()

                _uiState.value = DashboardUiState(
                    isLoading = false,
                    monthId = monthId,
                    totalBudget = overview.totalBudget,
                    totalSpent = overview.totalSpent,
                    remaining = overview.remaining,
                    percentageUsed = overview.percentageUsed,
                    isOverBudget = overview.isOverBudget,
                    topCategories = topCategories,
                    streak = gamification.currentStreak,
                    badges = gamification.badgesEarned
                )

            } catch (e: Exception) {
                _uiState.value = DashboardUiState(
                    isLoading = false,
                    error = "Failed to load dashboard"
                )
            }
        }
    }
}
