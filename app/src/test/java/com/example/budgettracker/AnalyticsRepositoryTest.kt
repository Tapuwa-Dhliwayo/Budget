package com.example.budgettracker

import com.example.budgettracker.data.repository.AnalyticsRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AnalyticsRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: AnalyticsRepository
    private lateinit var fakeExpenseDao: FakeExpenseDao
    private lateinit var fakeCategoryDao: FakeCategoryDao
    private lateinit var fakeMonthlyBudgetDao: FakeMonthlyBudgetDao

    @Before
    fun setup() {
        fakeExpenseDao = FakeExpenseDao()
        fakeCategoryDao = FakeCategoryDao()
        fakeMonthlyBudgetDao = FakeMonthlyBudgetDao()

        repository = AnalyticsRepository(
            fakeExpenseDao,
            fakeCategoryDao,
            fakeMonthlyBudgetDao
        )
    }

    @Test
    fun getMonthlyOverview_calculatesCorrectly() = runTest {
        // Arrange: Setup test data
        val categoryId = fakeCategoryDao.insertCategory(
            com.example.budgettracker.data.entity.CategoryEntity(
                name = "Groceries",
                color = 0xFF4CAF50.toInt(),
                icon = "🛒",
                budgetLimit = 600.0
            )
        )

        fakeExpenseDao.insertExpense(
            com.example.budgettracker.data.entity.ExpenseEntity(
                amount = 450.0,
                description = "Shopping",
                date = "2026-01-15",
                categoryId = categoryId
            )
        )
        fakeMonthlyBudgetDao.insertMonth(
            com.example.budgettracker.data.entity.MonthlyBudgetEntity(
                monthId = "2026-01",
                startDate = "2026-01-01",
                startingFunds = 600.0
            )
        )

        // Act: Get monthly overview
        val overview = repository.getMonthlyOverview("2026-01")

        // Assert: Verify calculations
        assertEquals(600.0, overview.totalBudget, 0.01)
        assertEquals(450.0, overview.totalSpent, 0.01)
        assertEquals(150.0, overview.remaining, 0.01)
        assertEquals(75.0, overview.percentageUsed, 0.1)
        assertFalse(overview.isOverBudget)
    }

    @Test
    fun getCategorySpending_returnsCorrectData() = runTest {
        // Arrange
        val categoryId = fakeCategoryDao.insertCategory(
            com.example.budgettracker.data.entity.CategoryEntity(
                name = "Groceries",
                color = 0xFF4CAF50.toInt(),
                icon = "🛒",
                budgetLimit = 600.0
            )
        )

        fakeExpenseDao.insertExpense(
            com.example.budgettracker.data.entity.ExpenseEntity(
                amount = 450.0,
                description = "Shopping",
                date = "2026-01-15",
                categoryId = categoryId
            )
        )

        // Act
        val spending = repository.getCategorySpending("2026-01")

        // Assert
        assertEquals(1, spending.size)
        assertEquals("Groceries", spending[0].categoryName)
        assertEquals(450.0, spending[0].totalSpent, 0.01)
        assertEquals(600.0, spending[0].budgetLimit, 0.01)
        assertFalse(spending[0].isOverBudget)
    }
    @Test
    fun getMonthlyOverview_usesConfiguredStartDateWindow() = runTest {
        val categoryId = fakeCategoryDao.insertCategory(
            com.example.budgettracker.data.entity.CategoryEntity(
                name = "General",
                color = 0xFF4CAF50.toInt(),
                icon = "•",
                budgetLimit = 1000.0
            )
        )

        fakeMonthlyBudgetDao.insertMonth(
            com.example.budgettracker.data.entity.MonthlyBudgetEntity(
                monthId = "2026-05",
                startDate = "2026-04-27",
                startingFunds = 1000.0
            )
        )

        fakeExpenseDao.insertExpense(
            com.example.budgettracker.data.entity.ExpenseEntity(
                amount = 120.0,
                description = "Included",
                date = "2026-05-01",
                categoryId = categoryId
            )
        )
        fakeExpenseDao.insertExpense(
            com.example.budgettracker.data.entity.ExpenseEntity(
                amount = 80.0,
                description = "Excluded",
                date = "2026-05-28",
                categoryId = categoryId
            )
        )

        val overview = repository.getMonthlyOverview("2026-05")

        assertEquals(120.0, overview.totalSpent, 0.01)
        assertEquals(880.0, overview.remaining, 0.01)
    }

}
