package com.example.budgettracker.ui.gamification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.budgettracker.data.repository.GamificationRepository

class GamificationViewModelFactory(
    private val repository: GamificationRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GamificationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GamificationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
