package com.example.budgettracker.ui.debt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.budgettracker.data.repository.DebtRepository

class DebtBossViewModelFactory(
    private val repository: DebtRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DebtBossViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DebtBossViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
