package com.example.budgettracker

import com.example.budgettracker.data.dao.ExtraIncomeDao
import com.example.budgettracker.data.entity.ExtraIncomeEntity

class FakeExtraIncomeDao : ExtraIncomeDao {
    private val entries = mutableListOf<ExtraIncomeEntity>()
    private var nextId = 1L

    override suspend fun getIncomeForPeriod(startDate: String, endDate: String): List<ExtraIncomeEntity> {
        return entries
            .filter { it.dateReceived >= startDate && it.dateReceived <= endDate }
            .sortedWith(compareByDescending<ExtraIncomeEntity> { it.dateReceived }.thenByDescending { it.id })
    }

    override suspend fun getRecentIncome(limit: Int): List<ExtraIncomeEntity> {
        return entries
            .sortedWith(compareByDescending<ExtraIncomeEntity> { it.dateReceived }.thenByDescending { it.id })
            .take(limit)
    }

    override suspend fun getIncomeById(id: Long): ExtraIncomeEntity? {
        return entries.find { it.id == id }
    }

    override suspend fun insertIncome(income: ExtraIncomeEntity): Long {
        val id = nextId++
        entries.add(income.copy(id = id))
        return id
    }

    override suspend fun updateIncome(income: ExtraIncomeEntity) {
        val index = entries.indexOfFirst { it.id == income.id }
        if (index >= 0) {
            entries[index] = income
        }
    }
}
