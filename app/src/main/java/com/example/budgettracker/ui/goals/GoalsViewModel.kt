package com.example.budgettracker.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker.data.model.GoalContributionSourceType
import com.example.budgettracker.data.model.GoalPriority
import com.example.budgettracker.data.model.GoalType
import com.example.budgettracker.data.repository.GoalRepository
import com.example.budgettracker.utils.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GoalsViewModel(
    private val repository: GoalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoalsUiState(isLoading = true))
    val uiState: StateFlow<GoalsUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                _uiState.value = GoalsUiState(
                    summary = repository.getSummary(),
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load goals: ${e.message}"
                )
            }
        }
    }

    fun addGoal(
        name: String,
        goalType: GoalType,
        targetAmount: Double,
        currentAmount: Double,
        targetDate: String?,
        monthlyContributionTarget: Double,
        priority: GoalPriority,
        notes: String
    ) {
        viewModelScope.launch {
            try {
                repository.addGoal(
                    name = name,
                    goalType = goalType,
                    targetAmount = targetAmount,
                    currentAmount = currentAmount,
                    targetDate = targetDate,
                    monthlyContributionTarget = monthlyContributionTarget,
                    priority = priority,
                    notes = notes
                )
                load()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to add goal: ${e.message}")
            }
        }
    }

    fun addContribution(goalId: Long, amount: Double, sourceType: GoalContributionSourceType, notes: String) {
        viewModelScope.launch {
            try {
                repository.addContribution(
                    goalId = goalId,
                    amount = amount,
                    contributionDate = DateUtils.getCurrentDate(),
                    sourceType = sourceType,
                    notes = notes
                )
                load()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to add contribution: ${e.message}")
            }
        }
    }
}
