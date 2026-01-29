package com.example.budgettracker.ui.analytics

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker.data.repository.AnalyticsRepository
import com.example.budgettracker.utils.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnalyticsViewModel(
    private val analyticsRepository: AnalyticsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun load(monthId: String = DateUtils.getCurrentMonthId()) {
        viewModelScope.launch {
            _uiState.value = AnalyticsUiState(isLoading = true)
            try {
                val data = analyticsRepository.getCategorySpending(monthId)
                _uiState.value = AnalyticsUiState(categories = data)
            } catch (e: Exception) {
                _uiState.value =
                    AnalyticsUiState(error = "Failed to load analytics")
            }
        }
    }
}
