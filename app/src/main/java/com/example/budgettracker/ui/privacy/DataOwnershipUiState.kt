package com.example.budgettracker.ui.privacy

import com.example.budgettracker.data.model.BackupStatus

data class DataOwnershipUiState(
    val backupStatus: BackupStatus? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null
)
