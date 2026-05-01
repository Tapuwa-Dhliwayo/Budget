package com.example.budgettracker.ui.privacy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker.data.model.RecoveryXpEventType
import com.example.budgettracker.data.repository.BackupRepository
import com.example.budgettracker.data.repository.GamificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DataOwnershipViewModel(
    private val backupRepository: BackupRepository,
    private val gamificationRepository: GamificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DataOwnershipUiState(isLoading = true))
    val uiState: StateFlow<DataOwnershipUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null, message = null)
                _uiState.value = DataOwnershipUiState(
                    backupStatus = backupRepository.getStatus(),
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load backup status: ${e.message}"
                )
            }
        }
    }

    fun createBackup() {
        viewModelScope.launch {
            try {
                val status = backupRepository.createBackup()
                gamificationRepository.recordRecoveryEvent(
                    eventType = RecoveryXpEventType.BACKUP_CREATED,
                    message = "Backup created. Your data stays yours."
                )
                _uiState.value = DataOwnershipUiState(
                    backupStatus = status,
                    isLoading = false,
                    message = "Backup created"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Backup failed: ${e.message}"
                )
            }
        }
    }
}
