package com.example.budgettracker.ui.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker.data.repository.AnalyticsRepository
import com.example.budgettracker.data.repository.BudgetRepository
import com.example.budgettracker.data.repository.GamificationRepository
import com.example.budgettracker.data.repository.UserProfileRepository
import com.example.budgettracker.data.repository.WeeklyAllowanceRepository
import com.example.budgettracker.utils.CurrencyUtils
import com.example.budgettracker.utils.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val budgetRepository: BudgetRepository,
    private val analyticsRepository: AnalyticsRepository,
    private val gamificationRepository: GamificationRepository,
    private val userProfileRepository: UserProfileRepository,
    private val weeklyAllowanceRepository: WeeklyAllowanceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadDashboard(monthId: String = DateUtils.getCurrentMonthId()) {
        viewModelScope.launch {
            _uiState.value = DashboardUiState(isLoading = true, monthId = monthId)

            try {
                // Users should set their budget via Edit Budget dialog
                val existingMonth = budgetRepository.getMonth(monthId)
                if (existingMonth == null) {
                    // Create month with 0.0 - user must set budget via Edit Budget
                    budgetRepository.loadOrCreateMonth(monthId, 0.0)
                }

                // Get user name
                val user = userProfileRepository.getOrCreateUser()
                val userName = user.firstName

                // Analytics
                val overview = analyticsRepository.getMonthlyOverview(monthId)
                val topCategories =
                    analyticsRepository.getCategorySpending(monthId).take(3)
                val allCategories = analyticsRepository.getCategorySpending(monthId)

                //Get Daily spending Trends
                val dailyTrend = analyticsRepository.getDailySpendingTrend(monthId, 7)

                // Get month comparison
                val comparison = analyticsRepository.compareWithPreviousMonth(monthId)
                val comparisonText = if (comparison.previousMonth != null) {
                    val diff = comparison.difference
                    val sign = if (diff >= 0) "+" else ""
                    val percentage = String.format("%.1f", comparison.percentageChange)
                    "$sign${CurrencyUtils.format(diff)} ($sign$percentage%) vs last month"
                } else {
                    "No previous month data"
                }

                // Gamification
                val gamification = gamificationRepository.getGamificationStatus()
                val weeklyAllowance = weeklyAllowanceRepository.getCurrentWeekSummary()
                val weeklyAllowanceHistory = weeklyAllowanceRepository.getRecentWeekSummaries(limit = 4)

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
                    badges = gamification.badgesEarned,
                    dailySpendingTrend = dailyTrend,
                    categoryBreakdown = allCategories,
                    previousMonthComparison = comparisonText,
                    userName = userName,
                    weeklyAllowance = weeklyAllowance,
                    weeklyAllowanceHistory = weeklyAllowanceHistory
                )

            } catch (e: Exception) {
                _uiState.value = DashboardUiState(
                    isLoading = false,
                    error = "Failed to load dashboard"
                )
            }
        }
    }

    /**
     * Update Budget for current month
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateBudget(monthId: String, newBudget: Double) {
        viewModelScope.launch {
            try {
                budgetRepository.updateMonthBudget(monthId, newBudget)
                loadDashboard(monthId) // Reload dashboard
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update budget: ${e.message}"
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateWeeklyAllowance(amount: Double) {
        viewModelScope.launch {
            try {
                weeklyAllowanceRepository.setCurrentWeekAllowance(amount)
                loadDashboard(_uiState.value.monthId.ifBlank { DateUtils.getCurrentMonthId() })
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update weekly allowance: ${e.message}"
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveWeeklyReview(
        wentWell: String,
        challenge: String,
        nextWeekAdjustment: String
    ) {
        viewModelScope.launch {
            try {
                weeklyAllowanceRepository.saveCurrentWeekReview(
                    wentWell = wentWell,
                    challenge = challenge,
                    nextWeekAdjustment = nextWeekAdjustment
                )
                loadDashboard(_uiState.value.monthId.ifBlank { DateUtils.getCurrentMonthId() })
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to save weekly review: ${e.message}"
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateWeeklyCategoryLimit(categoryId: Long, limitAmount: Double) {
        viewModelScope.launch {
            try {
                weeklyAllowanceRepository.setCurrentWeekCategoryLimit(categoryId, limitAmount)
                loadDashboard(_uiState.value.monthId.ifBlank { DateUtils.getCurrentMonthId() })
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update category allowance: ${e.message}"
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setRecoveryActionCompleted(actionText: String, isCompleted: Boolean) {
        viewModelScope.launch {
            try {
                weeklyAllowanceRepository.setCurrentWeekRecoveryActionCompleted(actionText, isCompleted)
                loadDashboard(_uiState.value.monthId.ifBlank { DateUtils.getCurrentMonthId() })
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update recovery action: ${e.message}"
                )
            }
        }
    }
}
