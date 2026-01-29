package com.example.budgettracker.ui.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker.data.model.CategorySummary
import com.example.budgettracker.data.model.GamificationStatus
import com.example.budgettracker.data.model.MonthlyOverview
import com.example.budgettracker.data.repository.AnalyticsRepository
import com.example.budgettracker.data.repository.GamificationRepository
import com.example.budgettracker.utils.DateUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SummaryUiState(
    val isLoading: Boolean = true,
    val monthlyOverview: MonthlyOverview? = null,
    val categorySummaries: List<CategorySummary> = emptyList(),
    val topCategories: List<CategorySummary> = emptyList(),
    val gamificationStatus: GamificationStatus? = null,
    val error: String? = null
)

class SummaryViewModel(
    private val analyticsRepository: AnalyticsRepository,
    private val gamificationRepository: GamificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SummaryUiState())
    val uiState: StateFlow<SummaryUiState> = _uiState.asStateFlow()

    init {
        loadSummary()
    }

    fun loadSummary(monthId: String = DateUtils.getCurrentMonthId()) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                // Load analytics data
                val overview = analyticsRepository.getMonthlyOverview(monthId)
                val summaries = analyticsRepository.getCategorySummaries(monthId)
                val topCategories = analyticsRepository.getTopSpendingCategories(monthId, 3)
                
                // Load gamification status
                val gamification = gamificationRepository.getGamificationStatus()
                
                _uiState.value = SummaryUiState(
                    isLoading = false,
                    monthlyOverview = overview,
                    categorySummaries = summaries,
                    topCategories = topCategories,
                    gamificationStatus = gamification
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load summary: ${e.message}"
                )
            }
        }
    }

    fun refreshSummary() {
        loadSummary()
    }
}
