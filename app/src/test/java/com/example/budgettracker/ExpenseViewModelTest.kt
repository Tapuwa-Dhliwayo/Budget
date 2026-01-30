package com.example.budgettracker

import com.example.budgettracker.data.entity.CategoryEntity
import com.example.budgettracker.data.repository.ExpenseRepository
import com.example.budgettracker.data.repository.GamificationRepository
import com.example.budgettracker.ui.expenses.ExpenseViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExpenseViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ExpenseViewModel
    private lateinit var fakeExpenseDao: FakeExpenseDao
    private lateinit var fakeCategoryDao: FakeCategoryDao
    private lateinit var fakeGamificationDao: FakeGamificationDao

    @Before
    fun setup() {
        fakeExpenseDao = FakeExpenseDao()
        fakeCategoryDao = FakeCategoryDao()
        fakeGamificationDao = FakeGamificationDao()

        // Setup a test category
        val expenseRepository = ExpenseRepository(fakeExpenseDao, fakeCategoryDao)
        val gamificationRepository = GamificationRepository(fakeGamificationDao)

        viewModel = ExpenseViewModel(expenseRepository, gamificationRepository)
    }

    @Test
    fun addExpense_insertsExpenseSuccessfully() = runTest {
        val categoryId = fakeCategoryDao.insertCategory(
            CategoryEntity(
                name = "Groceries",
                color = 0xFF4CAF50.toInt(),
                icon = "🛒",
                budgetLimit = 600.0
            )
        )

        //Now we can await
        viewModel.addExpense(
            amount = 450.0,
            description = "Weekly shopping",
            date = "2026-01-29",
            categoryId = categoryId
        )

        val expenses = fakeExpenseDao.getAllExpenses()
        assertEquals(1, expenses.size)
        assertEquals(450.0, expenses[0].amount, 0.01)
        assertEquals("Weekly shopping", expenses[0].description)
    }

}