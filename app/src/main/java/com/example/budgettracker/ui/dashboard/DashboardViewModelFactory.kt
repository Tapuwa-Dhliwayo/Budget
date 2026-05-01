package com.example.budgettracker.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.budgettracker.data.repository.AnalyticsRepository
import com.example.budgettracker.data.repository.BudgetRepository
import com.example.budgettracker.data.repository.GamificationRepository
import com.example.budgettracker.data.repository.UserProfileRepository
import com.example.budgettracker.data.repository.WeeklyAllowanceRepository

class DashboardViewModelFactory(
    private val budgetRepository: BudgetRepository,
    private val analyticsRepository: AnalyticsRepository,
    private val gamificationRepository: GamificationRepository,
    private val userProfileRepository: UserProfileRepository,
    private val weeklyAllowanceRepository: WeeklyAllowanceRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(
                budgetRepository = budgetRepository,
                analyticsRepository = analyticsRepository,
                gamificationRepository = gamificationRepository,
                userProfileRepository = userProfileRepository,
                weeklyAllowanceRepository = weeklyAllowanceRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
