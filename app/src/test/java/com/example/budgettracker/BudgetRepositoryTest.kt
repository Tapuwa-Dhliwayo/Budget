package com.example.budgettracker

import com.example.budgettracker.data.repository.BudgetRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class BudgetRepositoryTest {

    @Test
    fun initializeReanchoredCycleIfMissing_copiesBudgetToNewWindowWhenMissing() = runTest {
        val dao = FakeMonthlyBudgetDao()
        val repository = BudgetRepository(dao)

        repository.loadOrCreateMonth("2026-05", 4500.0)

        repository.initializeReanchoredCycleIfMissing("2026-05", "2026-04")

        assertNotNull(dao.getMonth("2026-04"))
        assertEquals(4500.0, dao.getMonth("2026-04")?.startingFunds ?: 0.0, 0.01)
    }

    @Test
    fun initializeReanchoredCycleIfMissing_doesNothingWhenTargetExists() = runTest {
        val dao = FakeMonthlyBudgetDao()
        val repository = BudgetRepository(dao)

        repository.loadOrCreateMonth("2026-05", 4500.0)
        repository.loadOrCreateMonth("2026-04", 3000.0)

        repository.initializeReanchoredCycleIfMissing("2026-05", "2026-04")

        assertEquals(3000.0, dao.getMonth("2026-04")?.startingFunds ?: 0.0, 0.01)
    }

    @Test
    fun initializeReanchoredCycleIfMissing_ignoresBlankOrSameMonthIds() = runTest {
        val dao = FakeMonthlyBudgetDao()
        val repository = BudgetRepository(dao)

        repository.loadOrCreateMonth("2026-05", 4500.0)

        repository.initializeReanchoredCycleIfMissing("", "2026-04")
        repository.initializeReanchoredCycleIfMissing("2026-05", "2026-05")

        assertNull(dao.getMonth("2026-04"))
        assertEquals(4500.0, dao.getMonth("2026-05")?.startingFunds ?: 0.0, 0.01)
    }
}
