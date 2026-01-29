package com.example.budgettracker.ui.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.budgettracker.data.repository.AnalyticsRepository
import com.example.budgettracker.data.repository.GamificationRepository

class SummaryViewModelFactory(
    private val analyticsRepository: AnalyticsRepository,
    private val gamificationRepository: GamificationRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SummaryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SummaryViewModel(analyticsRepository, gamificationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}