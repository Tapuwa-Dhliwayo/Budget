package com.example.budgettracker

import com.example.budgettracker.data.repository.AnalyticsRepository
import com.example.budgettracker.data.repository.BudgetRepository
import com.example.budgettracker.data.repository.GamificationRepository
import com.example.budgettracker.data.repository.UserProfileRepository
import com.example.budgettracker.ui.dashboard.DashboardViewModel
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DashboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadDashboard_createsMonthIfMissing_andUpdatesUiState() = runTest {
        // Arrange
        val monthlyBudgetDao = FakeMonthlyBudgetDao()
        val budgetRepository = BudgetRepository(monthlyBudgetDao)

        val analyticsRepository = AnalyticsRepository(
            expenseDao = FakeExpenseDao(),
            categoryDao = FakeCategoryDao(),
            monthlyBudgetDao = monthlyBudgetDao
        )

        val gamificationDao = FakeGamificationDao()
        val gamificationRepository = GamificationRepository(gamificationDao)

        val userProfileDao = FakeUserProfileDao()
        val userProfileRepository = UserProfileRepository(userProfileDao)

        val viewModel = DashboardViewModel(
            budgetRepository = budgetRepository,
            analyticsRepository = analyticsRepository,
            gamificationRepository = gamificationRepository,
            userProfileRepository = userProfileRepository
        )

        // Act
        viewModel.loadDashboard("2026-01")
        advanceUntilIdle()

        // Assert: month created
        val savedMonth = monthlyBudgetDao.getMonth("2026-01")
        assertEquals("2026-01", savedMonth?.monthId)
        assertEquals(0.0, savedMonth?.startingFunds ?: 0.0, 0.01)

        // Assert: UI state updated
        val uiState = viewModel.uiState.value
        assertEquals(false, uiState.isLoading)
        assertEquals(null, uiState.error)
        assertEquals("2026-01", uiState.monthId)
    }
}
