package com.example.budgettracker.data.repository

import com.example.budgettracker.data.dao.MonthlyBudgetDao
import com.example.budgettracker.data.entity.MonthlyBudgetEntity

class BudgetRepository(
    private val dao: MonthlyBudgetDao
) {
    suspend fun loadOrCreateMonth(
        monthId: String,
        startingFunds: Double
    ): MonthlyBudgetEntity {

        val existingMonth = dao.getMonth(monthId)
        if (existingMonth != null) {
            return existingMonth
        }

        val newMonth = MonthlyBudgetEntity(
            monthId = monthId,
            startDate = "$monthId-01",
            startingFunds = startingFunds
        )

        dao.insertMonth(newMonth)
        return newMonth
    }
}