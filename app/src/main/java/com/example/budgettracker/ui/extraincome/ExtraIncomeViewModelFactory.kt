package com.example.budgettracker.ui.extraincome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.budgettracker.data.repository.ExtraIncomeRepository

class ExtraIncomeViewModelFactory(
    private val repository: ExtraIncomeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExtraIncomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExtraIncomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
