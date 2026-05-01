package com.example.budgettracker.ui.privacy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.budgettracker.data.repository.BackupRepository
import com.example.budgettracker.data.repository.GamificationRepository

class DataOwnershipViewModelFactory(
    private val backupRepository: BackupRepository,
    private val gamificationRepository: GamificationRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DataOwnershipViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DataOwnershipViewModel(backupRepository, gamificationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
