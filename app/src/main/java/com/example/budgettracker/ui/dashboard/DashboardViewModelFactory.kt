package com.example.budgettracker.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.budgettracker.data.repository.AnalyticsRepository
import com.example.budgettracker.data.repository.BudgetRepository

class DashboardViewModelFactory(
    private val budgetRepository: BudgetRepository,
    private val analyticsRepository: AnalyticsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(budgetRepository, analyticsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}