package com.example.budgettracker

import com.example.budgettracker.data.entity.ExpenseEntity
import com.example.budgettracker.data.model.WeeklyAllowanceStatus
import com.example.budgettracker.data.repository.WeeklyAllowanceRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class WeeklyAllowanceRepositoryTest {

    private lateinit var fakeWeeklyAllowanceDao: FakeWeeklyAllowanceDao
    private lateinit var fakeWeeklyReviewDao: FakeWeeklyReviewDao
    private lateinit var fakeWeeklyCategoryAllowanceDao: FakeWeeklyCategoryAllowanceDao
    private lateinit var fakeWeeklyRecoveryActionDao: FakeWeeklyRecoveryActionDao
    private lateinit var fakeExpenseDao: FakeExpenseDao
    private lateinit var fakeCategoryDao: FakeCategoryDao
    private lateinit var repository: WeeklyAllowanceRepository

    @Before
    fun setup() {
        fakeWeeklyAllowanceDao = FakeWeeklyAllowanceDao()
        fakeWeeklyReviewDao = FakeWeeklyReviewDao()
        fakeWeeklyCategoryAllowanceDao = FakeWeeklyCategoryAllowanceDao()
        fakeWeeklyRecoveryActionDao = FakeWeeklyRecoveryActionDao()
        fakeExpenseDao = FakeExpenseDao()
        fakeCategoryDao = FakeCategoryDao()
        repository = WeeklyAllowanceRepository(
            fakeWeeklyAllowanceDao,
            fakeWeeklyReviewDao,
            fakeWeeklyCategoryAllowanceDao,
            fakeWeeklyRecoveryActionDao,
            fakeExpenseDao,
            fakeCategoryDao
        )
    }

    @Test
    fun getWeekSummary_withoutAllowance_returnsNotSetSummary() = runTest {
        val summary = repository.getWeekSummary(
            weekStart = LocalDate.parse("2026-01-26"),
            today = LocalDate.parse("2026-01-29")
        )

        assertFalse(summary.allowanceSet)
        assertEquals(WeeklyAllowanceStatus.NOT_SET, summary.status)
        assertEquals("2026-01-26", summary.weekStartDate)
        assertEquals("2026-02-01", summary.weekEndDate)
    }

    @Test
    fun getWeekSummary_withAllowanceAndSteadySpend_returnsStable() = runTest {
        fakeCategoryDao.insertCategory(
            com.example.budgettracker.data.entity.CategoryEntity(
                name = "Groceries",
                color = 0,
                icon = "G",
                budgetLimit = 0.0
            )
        )
        repository.setCurrentWeekAllowance(
            amount = 700.0,
            today = LocalDate.parse("2026-01-29")
        )
        fakeExpenseDao.insertExpense(
            ExpenseEntity(
                amount = 300.0,
                description = "Groceries",
                date = "2026-01-28",
                categoryId = 1L
            )
        )

        val summary = repository.getWeekSummary(
            weekStart = LocalDate.parse("2026-01-26"),
            today = LocalDate.parse("2026-01-29")
        )

        assertTrue(summary.allowanceSet)
        assertEquals(WeeklyAllowanceStatus.STABLE, summary.status)
        assertEquals(700.0, summary.allowanceAmount, 0.01)
        assertEquals(300.0, summary.spent, 0.01)
        assertEquals(400.0, summary.remaining, 0.01)
        assertEquals(100.0, summary.safeDailySpend, 0.01)
        assertEquals(4, summary.daysRemaining)
    }

    @Test
    fun getWeekSummary_whenSpentExceedsAllowance_returnsOverPlan() = runTest {
        fakeCategoryDao.insertCategory(
            com.example.budgettracker.data.entity.CategoryEntity(
                name = "Transport",
                color = 0,
                icon = "T",
                budgetLimit = 0.0
            )
        )
        repository.setCurrentWeekAllowance(
            amount = 500.0,
            today = LocalDate.parse("2026-01-29")
        )
        fakeExpenseDao.insertExpense(
            ExpenseEntity(
                amount = 650.0,
                description = "Transport repairs",
                date = "2026-01-29",
                categoryId = 1L
            )
        )

        val summary = repository.getWeekSummary(
            weekStart = LocalDate.parse("2026-01-26"),
            today = LocalDate.parse("2026-01-29")
        )

        assertEquals(WeeklyAllowanceStatus.OVER_PLAN, summary.status)
        assertEquals(-150.0, summary.remaining, 0.01)
        assertEquals(0.0, summary.safeDailySpend, 0.01)
    }

    @Test
    fun getWeekSummary_excludesRecurringExpensesFromWeeklySpend() = runTest {
        fakeCategoryDao.insertCategory(
            com.example.budgettracker.data.entity.CategoryEntity(
                name = "Groceries",
                color = 0,
                icon = "G",
                budgetLimit = 0.0
            )
        )
        fakeCategoryDao.insertCategory(
            com.example.budgettracker.data.entity.CategoryEntity(
                name = "Rent",
                color = 0,
                icon = "R",
                budgetLimit = 0.0
            )
        )
        repository.setCurrentWeekAllowance(
            amount = 700.0,
            today = LocalDate.parse("2026-01-29")
        )
        fakeExpenseDao.insertExpense(
            ExpenseEntity(
                amount = 300.0,
                description = "Groceries",
                date = "2026-01-28",
                categoryId = 1L
            )
        )
        fakeExpenseDao.insertExpense(
            ExpenseEntity(
                amount = 600.0,
                description = "Rent",
                date = "2026-01-28",
                categoryId = 2L,
                isRecurring = true
            )
        )

        val summary = repository.getWeekSummary(
            weekStart = LocalDate.parse("2026-01-26"),
            today = LocalDate.parse("2026-01-29")
        )

        assertEquals(300.0, summary.spent, 0.01)
        assertEquals(400.0, summary.remaining, 0.01)
        assertEquals(WeeklyAllowanceStatus.STABLE, summary.status)
    }

    @Test
    fun getWeekSummary_includesMainCategoryPressureAndRecoveryActions() = runTest {
        fakeCategoryDao.insertCategory(
            com.example.budgettracker.data.entity.CategoryEntity(
                name = "Takeaways",
                color = 0,
                icon = "T",
                budgetLimit = 0.0
            )
        )
        repository.setCurrentWeekAllowance(
            amount = 700.0,
            today = LocalDate.parse("2026-01-29")
        )
        fakeExpenseDao.insertExpense(
            ExpenseEntity(
                amount = 450.0,
                description = "Dinner",
                date = "2026-01-29",
                categoryId = 1L
            )
        )

        val summary = repository.getWeekSummary(
            weekStart = LocalDate.parse("2026-01-26"),
            today = LocalDate.parse("2026-01-29")
        )

        assertEquals("Takeaways", summary.categoryPressures.first().categoryName)
        assertEquals(450.0, summary.categoryPressures.first().amount, 0.01)
        assertTrue(summary.recoveryActions.any { it.actionText.contains("Takeaways") })
    }

    @Test
    fun saveCurrentWeekReview_attachesReviewToSummary() = runTest {
        repository.setCurrentWeekAllowance(
            amount = 700.0,
            today = LocalDate.parse("2026-01-29")
        )
        repository.saveCurrentWeekReview(
            wentWell = "Logged everything",
            challenge = "Takeaways",
            nextWeekAdjustment = "Cook twice",
            today = LocalDate.parse("2026-01-29")
        )

        val summary = repository.getWeekSummary(
            weekStart = LocalDate.parse("2026-01-26"),
            today = LocalDate.parse("2026-01-29")
        )

        assertEquals("Cook twice", summary.review?.nextWeekAdjustment)
    }

    @Test
    fun setCurrentWeekCategoryLimit_marksCategoryAsOverLimit() = runTest {
        fakeCategoryDao.insertCategory(
            com.example.budgettracker.data.entity.CategoryEntity(
                name = "Takeaways",
                color = 0,
                icon = "T",
                budgetLimit = 0.0
            )
        )
        repository.setCurrentWeekAllowance(700.0, LocalDate.parse("2026-01-29"))
        repository.setCurrentWeekCategoryLimit(
            categoryId = 1L,
            limitAmount = 100.0,
            today = LocalDate.parse("2026-01-29")
        )
        fakeExpenseDao.insertExpense(
            ExpenseEntity(
                amount = 150.0,
                description = "Dinner",
                date = "2026-01-29",
                categoryId = 1L
            )
        )

        val summary = repository.getWeekSummary(
            weekStart = LocalDate.parse("2026-01-26"),
            today = LocalDate.parse("2026-01-29")
        )

        assertEquals(100.0, summary.categoryPressures.first().weeklyLimit ?: 0.0, 0.01)
        assertTrue(summary.categoryPressures.first().isOverLimit)
    }

    @Test
    fun setCurrentWeekRecoveryActionCompleted_marksMatchingActionComplete() = runTest {
        repository.setCurrentWeekAllowance(700.0, LocalDate.parse("2026-01-29"))
        val initial = repository.getWeekSummary(
            weekStart = LocalDate.parse("2026-01-26"),
            today = LocalDate.parse("2026-01-29")
        )
        val action = initial.recoveryActions.first().actionText

        repository.setCurrentWeekRecoveryActionCompleted(
            actionText = action,
            isCompleted = true,
            today = LocalDate.parse("2026-01-29")
        )

        val updated = repository.getWeekSummary(
            weekStart = LocalDate.parse("2026-01-26"),
            today = LocalDate.parse("2026-01-29")
        )

        assertTrue(updated.recoveryActions.first { it.actionText == action }.isCompleted)
    }
}
