package com.example.budgettracker.data.repository

import com.example.budgettracker.data.dao.CategoryDao
import com.example.budgettracker.data.dao.ExpenseDao
import com.example.budgettracker.data.entity.ExpenseEntity
import com.example.budgettracker.data.model.ExpenseWithCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ExpenseRepository(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao
) {
    
    fun observeAllExpenses(): Flow<List<ExpenseEntity>> {
        return expenseDao.observeAllExpenses()
    }
    
    fun observeExpensesWithCategory(): Flow<List<ExpenseWithCategory>> {
        return combine(
            expenseDao.observeAllExpenses(),
            categoryDao.observeAllCategories()
        ) { expenses, categories ->
            val categoryMap = categories.associateBy { it.id }
            expenses.mapNotNull { expense ->
                val category = categoryMap[expense.categoryId] ?: return@mapNotNull null
                ExpenseWithCategory(
                    expenseId = expense.id,
                    amount = expense.amount,
                    description = expense.description,
                    date = expense.date,
                    photoPath = expense.photoPath,
                    categoryName = category.name,
                    categoryColor = category.color,
                    categoryIcon = category.icon
                )
            }
        }
    }
    
    fun observeExpensesByDateRange(startDate: String, endDate: String): Flow<List<ExpenseEntity>> {
        return expenseDao.observeExpensesByDateRange(startDate, endDate)
    }
    
    suspend fun getExpenseById(id: Long): ExpenseEntity? {
        return expenseDao.getExpenseById(id)
    }
    
    suspend fun insertExpense(expense: ExpenseEntity): Long {
        return expenseDao.insertExpense(expense)
    }
    
    suspend fun updateExpense(expense: ExpenseEntity) {
        expenseDao.updateExpense(expense)
    }
    
    suspend fun deleteExpense(expense: ExpenseEntity) {
        expenseDao.deleteExpense(expense)
    }
    
    suspend fun getTotalSpendingForPeriod(startDate: String, endDate: String): Double {
        return expenseDao.getTotalSpendingForPeriod(startDate, endDate) ?: 0.0
    }
}
