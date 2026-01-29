package com.example.budgettracker.ui.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.budgettracker.data.repository.ExpenseRepository
import com.example.budgettracker.data.repository.GamificationRepository

class ExpenseViewModelFactory(
    private val expenseRepository: ExpenseRepository,
    private val gamificationRepository: GamificationRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(expenseRepository, gamificationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}