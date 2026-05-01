package com.example.budgettracker.ui.gamification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker.data.repository.GamificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GamificationViewModel(
    private val repository: GamificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GamificationUiState(isLoading = true))
    val uiState: StateFlow<GamificationUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                _uiState.value = GamificationUiState(
                    status = repository.getGamificationStatus(),
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load recovery progress: ${e.message}"
                )
            }
        }
    }
}
