package com.example.budgettracker

import com.example.budgettracker.data.dao.DebtPaymentDao
import com.example.budgettracker.data.entity.DebtPaymentEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeDebtPaymentDao : DebtPaymentDao {
    private val payments = mutableListOf<DebtPaymentEntity>()
    private var nextId = 1L

    override fun observePaymentsForDebt(debtId: Long): Flow<List<DebtPaymentEntity>> {
        return MutableStateFlow(payments.filter { it.debtId == debtId })
    }

    override suspend fun getPaymentsForDebt(debtId: Long): List<DebtPaymentEntity> {
        return payments.filter { it.debtId == debtId }
    }

    override suspend fun getPaymentsForPeriod(
        startDate: String,
        endDate: String
    ): List<DebtPaymentEntity> {
        return payments.filter { it.date >= startDate && it.date <= endDate }
    }

    override suspend fun insertPayment(payment: DebtPaymentEntity): Long {
        val id = nextId++
        payments.add(payment.copy(id = id))
        return id
    }
}
