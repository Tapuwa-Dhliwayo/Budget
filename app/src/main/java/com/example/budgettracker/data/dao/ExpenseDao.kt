package com.example.budgettracker.data.dao

import androidx.room.*
import com.example.budgettracker.data.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    
    @Query("SELECT * FROM expenses ORDER BY date DESC, createdAt DESC")
    fun observeAllExpenses(): Flow<List<ExpenseEntity>>
    
    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun observeExpensesByDateRange(startDate: String, endDate: String): Flow<List<ExpenseEntity>>
    
    @Query("SELECT * FROM expenses WHERE categoryId = :categoryId ORDER BY date DESC")
    fun observeExpensesByCategory(categoryId: Long): Flow<List<ExpenseEntity>>
    
    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getExpenseById(id: Long): ExpenseEntity?
    
    @Insert
    suspend fun insertExpense(expense: ExpenseEntity): Long
    
    @Update
    suspend fun updateExpense(expense: ExpenseEntity)
    
    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)
    
    // Analytics queries
    @Query("SELECT SUM(amount) FROM expenses WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getTotalSpendingForPeriod(startDate: String, endDate: String): Double?

    @Query("SELECT SUM(amount) FROM expenses WHERE isRecurring = 0 AND date BETWEEN :startDate AND :endDate")
    suspend fun getNonRecurringSpendingForPeriod(startDate: String, endDate: String): Double?

    @Query("SELECT * FROM expenses WHERE isRecurring = 0 AND date BETWEEN :startDate AND :endDate")
    suspend fun getNonRecurringExpensesForPeriod(startDate: String, endDate: String): List<ExpenseEntity>
    
    @Query("SELECT SUM(amount) FROM expenses WHERE categoryId = :categoryId AND date BETWEEN :startDate AND :endDate")
    suspend fun getCategorySpendingForPeriod(categoryId: Long, startDate: String, endDate: String): Double?
    
    @Query("SELECT COUNT(DISTINCT date) FROM expenses WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getUniqueDaysWithExpenses(startDate: String, endDate: String): Int
    
    @Query("SELECT * FROM expenses WHERE date = :date ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLastExpenseForDate(date: String): ExpenseEntity?
}
