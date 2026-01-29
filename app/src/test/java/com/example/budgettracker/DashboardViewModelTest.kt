package com.example.budgettracker

import com.example.budgettracker.data.repository.BudgetRepository
import com.example.budgettracker.ui.dashboard.DashboardViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class DashboardViewModelTest {

    @get:Rule
    val mainDispatcherRule: TestRule = MainDispatcherRule()

    @Test
    fun loadMonth_createsMonthIfMissing() = runTest {
        val fakeDao = FakeMonthlyBudgetDao()
        val repository = BudgetRepository(fakeDao)
        val viewModel = DashboardViewModel(repository)

        viewModel.loadMonth("2026-01", 4000.0)

        val result = viewModel.monthState.value

        assertEquals("2026-01", result?.monthId)
        assertEquals(4000.0, result?.startingFunds)
    }
}