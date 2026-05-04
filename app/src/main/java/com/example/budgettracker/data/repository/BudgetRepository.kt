package com.example.budgettracker.data.repository

import com.example.budgettracker.data.dao.MonthlyBudgetDao
import com.example.budgettracker.data.entity.MonthlyBudgetEntity
import com.example.budgettracker.utils.DateUtils

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
            startDate = DateUtils.getMonthStartDate(monthId),
            startingFunds = startingFunds
        )

        dao.insertMonth(newMonth)
        return newMonth
    }

    suspend fun updateMonthBudget(monthId: String, newStartingFunds: Double) {
        val month = dao.getMonth(monthId)
        if (month != null) {
            val updatedMonth = month.copy(startingFunds = newStartingFunds)
            dao.updateMonth(updatedMonth)
        }
    }

    suspend fun getMonth(monthId: String): MonthlyBudgetEntity? {
        return dao.getMonth(monthId)
    }

    suspend fun initializeReanchoredCycleIfMissing(fromMonthId: String, toMonthId: String) {
        if (fromMonthId.isBlank() || toMonthId.isBlank() || fromMonthId == toMonthId) return

        val from = dao.getMonth(fromMonthId) ?: return
        val existingTarget = dao.getMonth(toMonthId)
        if (existingTarget != null) return

        dao.insertMonth(
            MonthlyBudgetEntity(
                monthId = toMonthId,
                startDate = DateUtils.getMonthStartDate(toMonthId),
                startingFunds = from.startingFunds
            )
        )
    }
}
