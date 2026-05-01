package com.example.budgettracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.budgettracker.data.entity.DebtPaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DebtPaymentDao {
    @Query("SELECT * FROM debt_payments WHERE debtId = :debtId ORDER BY date DESC, createdAt DESC")
    fun observePaymentsForDebt(debtId: Long): Flow<List<DebtPaymentEntity>>

    @Query("SELECT * FROM debt_payments WHERE debtId = :debtId ORDER BY date DESC, createdAt DESC")
    suspend fun getPaymentsForDebt(debtId: Long): List<DebtPaymentEntity>

    @Query("SELECT * FROM debt_payments WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getPaymentsForPeriod(startDate: String, endDate: String): List<DebtPaymentEntity>

    @Insert
    suspend fun insertPayment(payment: DebtPaymentEntity): Long
}
