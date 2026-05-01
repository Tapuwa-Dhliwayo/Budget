package com.example.budgettracker

import com.example.budgettracker.data.dao.ExpenseDao
import com.example.budgettracker.data.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeExpenseDao : ExpenseDao {

    private val expenses = mutableListOf<ExpenseEntity>()
    private val flowData = MutableStateFlow<List<ExpenseEntity>>(emptyList())
    private var nextId = 1L

    override fun observeAllExpenses(): Flow<List<ExpenseEntity>> = flowData

    override fun observeExpensesByDateRange(startDate: String, endDate: String): Flow<List<ExpenseEntity>> {
        val filtered = expenses.filter { it.date >= startDate && it.date <= endDate }
        return MutableStateFlow(filtered)
    }

    override fun observeExpensesByCategory(categoryId: Long): Flow<List<ExpenseEntity>> {
        val filtered = expenses.filter { it.categoryId == categoryId }
        return MutableStateFlow(filtered)
    }

    override suspend fun getExpenseById(id: Long): ExpenseEntity? {
        return expenses.find { it.id == id }
    }

    override suspend fun insertExpense(expense: ExpenseEntity): Long {
        val id = nextId++
        expenses.add(expense.copy(id = id))
        updateFlow()
        return id
    }

    override suspend fun updateExpense(expense: ExpenseEntity) {
        val index = expenses.indexOfFirst { it.id == expense.id }
        if (index != -1) {
            expenses[index] = expense
            updateFlow()
        }
    }

    override suspend fun deleteExpense(expense: ExpenseEntity) {
        expenses.removeIf { it.id == expense.id }
        updateFlow()
    }

    override suspend fun getTotalSpendingForPeriod(startDate: String, endDate: String): Double? {
        return expenses
            .filter { it.date >= startDate && it.date <= endDate }
            .sumOf { it.amount }
            .takeIf { it > 0 }
    }

    override suspend fun getNonRecurringSpendingForPeriod(startDate: String, endDate: String): Double? {
        return expenses
            .filter { !it.isRecurring && it.date >= startDate && it.date <= endDate }
            .sumOf { it.amount }
            .takeIf { it > 0 }
    }

    override suspend fun getNonRecurringExpensesForPeriod(
        startDate: String,
        endDate: String
    ): List<ExpenseEntity> {
        return expenses.filter { !it.isRecurring && it.date >= startDate && it.date <= endDate }
    }

    override suspend fun getCategorySpendingForPeriod(
        categoryId: Long,
        startDate: String,
        endDate: String
    ): Double? {
        return expenses
            .filter { it.categoryId == categoryId && it.date >= startDate && it.date <= endDate }
            .sumOf { it.amount }
            .takeIf { it > 0 }
    }

    override suspend fun getUniqueDaysWithExpenses(startDate: String, endDate: String): Int {
        return expenses
            .filter { it.date >= startDate && it.date <= endDate }
            .map { it.date }
            .distinct()
            .size
    }

    override suspend fun getLastExpenseForDate(date: String): ExpenseEntity? {
        return expenses.filter { it.date == date }.maxByOrNull { it.createdAt }
    }

    fun getAllExpenses() = expenses.toList()
    fun clear() {
        expenses.clear()
        nextId = 1L
        updateFlow()
    }

    private fun updateFlow() {
        flowData.value = expenses.sortedByDescending { it.date }
    }
}
