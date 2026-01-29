package com.example.budgettracker

import com.example.budgettracker.data.model.CategorySpending
import com.example.budgettracker.data.model.MonthlyOverview
import com.example.budgettracker.data.repository.AnalyticsDataSource

class FakeAnalyticsRepository : AnalyticsDataSource {

    override suspend fun getMonthlyOverview(monthId: String): MonthlyOverview {
        return MonthlyOverview(
            monthId = monthId,
            totalBudget = 0.0,
            totalSpent = 0.0,
            remaining = 0.0,
            percentageUsed = 0.0,
            isOverBudget = false
        )
    }

    override suspend fun getCategorySpending(
        monthId: String
    ): List<CategorySpending> = emptyList()
}
