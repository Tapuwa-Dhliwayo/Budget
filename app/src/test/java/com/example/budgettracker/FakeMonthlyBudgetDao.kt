package com.example.budgettracker

import com.example.budgettracker.data.dao.MonthlyBudgetDao
import com.example.budgettracker.data.entity.MonthlyBudgetEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeMonthlyBudgetDao : MonthlyBudgetDao {

    private val data = mutableMapOf<String, MonthlyBudgetEntity>()
    private val flow = MutableStateFlow<List<MonthlyBudgetEntity>>(emptyList())

    override suspend fun insertMonth(month: MonthlyBudgetEntity) {
        data[month.monthId] = month
        flow.value = data.values.toList()
    }

    override suspend fun getMonth(monthId: String): MonthlyBudgetEntity? {
        return data[monthId]
    }

    override fun observeAllMonths(): Flow<List<MonthlyBudgetEntity>> {
        return flow
    }
}