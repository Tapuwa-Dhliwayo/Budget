package com.example.budgettracker.data.repository

import com.example.budgettracker.data.model.CategorySpending
import com.example.budgettracker.data.model.MonthlyOverview

interface AnalyticsDataSource {
    suspend fun getMonthlyOverview(monthId: String): MonthlyOverview
    suspend fun getCategorySpending(monthId: String): List<CategorySpending>
}
