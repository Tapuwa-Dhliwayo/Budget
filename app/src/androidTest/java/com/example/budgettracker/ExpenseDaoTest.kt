package com.example.budgettracker

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.budgettracker.data.dao.CategoryDao
import com.example.budgettracker.data.dao.ExpenseDao
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.entity.CategoryEntity
import com.example.budgettracker.data.entity.ExpenseEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExpenseDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var expenseDao: ExpenseDao
    private lateinit var categoryDao: CategoryDao
    private var categoryId: Long = 0

    @Before
    fun setup() = runTest {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        expenseDao = db.expenseDao()
        categoryDao = db.categoryDao()

        // Insert a category for foreign key constraint
        val category = CategoryEntity(
            name = "Groceries",
            color = 0xFF4CAF50.toInt(),
            icon = "🛒",
            budgetLimit = 600.0
        )
        categoryId = categoryDao.insertCategory(category)
    }

    @After
    fun teardown() {
        if (this::db.isInitialized) {
            db.close()
        }
    }

    @Test
    fun insertAndRetrieveExpense() = runTest {
        val expense = ExpenseEntity(
            amount = 450.0,
            description = "Weekly shopping",
            date = "2026-01-29",
            categoryId = categoryId
        )

        val id = expenseDao.insertExpense(expense)
        val loaded = expenseDao.getExpenseById(id)

        assertNotNull(loaded)
        assertEquals(450.0, loaded?.amount ?: 0.0, 0.01)
        assertEquals("Weekly shopping", loaded?.description)
    }

    @Test
    fun getTotalSpendingForPeriod() = runTest {
        val expenses = listOf(
            ExpenseEntity(amount = 100.0, description = "Test 1", date = "2026-01-15", categoryId = categoryId),
            ExpenseEntity(amount = 200.0, description = "Test 2", date = "2026-01-20", categoryId = categoryId),
            ExpenseEntity(amount = 300.0, description = "Test 3", date = "2026-01-25", categoryId = categoryId)
        )

        expenses.forEach { expenseDao.insertExpense(it) }

        val total = expenseDao.getTotalSpendingForPeriod("2026-01-01", "2026-01-31")
        assertEquals(600.0, total ?: 0.0, 0.01)
    }

    @Test
    fun getCategorySpendingForPeriod() = runTest {
        val expense = ExpenseEntity(
            amount = 450.0,
            description = "Groceries",
            date = "2026-01-29",
            categoryId = categoryId
        )

        expenseDao.insertExpense(expense)

        val total = expenseDao.getCategorySpendingForPeriod(categoryId, "2026-01-01", "2026-01-31")
        assertEquals(450.0, total ?: 0.0, 0.01)
    }

    @Test
    fun deleteExpense() = runTest {
        val expense = ExpenseEntity(
            amount = 450.0,
            description = "Test",
            date = "2026-01-29",
            categoryId = categoryId
        )

        val id = expenseDao.insertExpense(expense)
        val loaded = expenseDao.getExpenseById(id)
        assertNotNull(loaded)

        expenseDao.deleteExpense(loaded!!)

        val deleted = expenseDao.getExpenseById(id)
        assertNull(deleted)
    }
}