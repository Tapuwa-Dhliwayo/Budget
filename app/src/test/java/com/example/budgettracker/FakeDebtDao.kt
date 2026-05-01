package com.example.budgettracker

import com.example.budgettracker.data.dao.DebtDao
import com.example.budgettracker.data.entity.DebtEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeDebtDao : DebtDao {
    private val debts = mutableListOf<DebtEntity>()
    private val flow = MutableStateFlow<List<DebtEntity>>(emptyList())
    private var nextId = 1L

    override fun observeDebts(): Flow<List<DebtEntity>> = flow

    override suspend fun getAllDebts(): List<DebtEntity> = debts.toList()

    override suspend fun getDebtById(id: Long): DebtEntity? {
        return debts.find { it.id == id }
    }

    override suspend fun insertDebt(debt: DebtEntity): Long {
        val id = nextId++
        debts.add(debt.copy(id = id))
        updateFlow()
        return id
    }

    override suspend fun updateDebt(debt: DebtEntity) {
        val index = debts.indexOfFirst { it.id == debt.id }
        if (index >= 0) {
            debts[index] = debt
            updateFlow()
        }
    }

    private fun updateFlow() {
        flow.value = debts.toList()
    }
}
