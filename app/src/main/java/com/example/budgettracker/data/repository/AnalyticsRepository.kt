package com.example.budgettracker.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.budgettracker.data.dao.CategoryDao
import com.example.budgettracker.data.dao.ExpenseDao
import com.example.budgettracker.data.dao.MonthlyBudgetDao
import com.example.budgettracker.data.model.CategorySpending
import com.example.budgettracker.data.model.CategorySummary
import com.example.budgettracker.data.model.MonthlyOverview
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class AnalyticsRepository(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao,
    private val monthlyBudgetDao: MonthlyBudgetDao
) : AnalyticsDataSource {
    
    /**
     * Get spending breakdown by category for a month
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getCategorySpending(monthId: String): List<CategorySpending> {
        val (startDate, endDate) = getMonthDateRange(monthId)
        val categories = categoryDao.getAllCategories()
        
        return categories.map { category ->
            val spent = expenseDao.getCategorySpendingForPeriod(
                category.id,
                startDate,
                endDate
            ) ?: 0.0
            
            val percentageUsed = if (category.budgetLimit > 0) {
                (spent / category.budgetLimit) * 100
            } else 0.0
            
            val remaining = category.budgetLimit - spent
            
            CategorySpending(
                categoryId = category.id,
                categoryName = category.name,
                categoryColor = category.color,
                categoryIcon = category.icon,
                totalSpent = spent,
                budgetLimit = category.budgetLimit,
                percentageUsed = percentageUsed,
                remaining = remaining,
                isOverBudget = spent > category.budgetLimit
            )
        }.sortedByDescending { it.totalSpent }
    }
    
    /**
     * Get monthly overview with total budget vs spending
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getMonthlyOverview(monthId: String): MonthlyOverview {
        val (startDate, endDate) = getMonthDateRange(monthId)
        val monthlyBudget = monthlyBudgetDao.getMonth(monthId)
        val totalBudget = monthlyBudget?.startingFunds ?: 0.0
        
        val totalSpent = expenseDao.getTotalSpendingForPeriod(startDate, endDate) ?: 0.0
        val remaining = totalBudget - totalSpent
        val percentageUsed = if (totalBudget > 0) (totalSpent / totalBudget) * 100 else 0.0
        
        return MonthlyOverview(
            monthId = monthId,
            totalBudget = totalBudget,
            totalSpent = totalSpent,
            remaining = remaining,
            percentageUsed = percentageUsed,
            isOverBudget = totalSpent > totalBudget
        )
    }
    
    /**
     * Get category summaries for text-based analytics
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getCategorySummaries(monthId: String): List<CategorySummary> {
        val categorySpending = getCategorySpending(monthId)
        val totalSpent = categorySpending.sumOf { it.totalSpent }
        
        return categorySpending
            .filter { it.totalSpent > 0 }
            .map { spending ->
                val percentage = if (totalSpent > 0) {
                    (spending.totalSpent / totalSpent) * 100
                } else 0.0
                
                CategorySummary(
                    categoryName = spending.categoryName,
                    categoryIcon = spending.categoryIcon,
                    amount = spending.totalSpent,
                    percentage = percentage,
                    color = spending.categoryColor
                )
            }
            .sortedByDescending { it.amount }
    }
    
    /**
     * Get top N spending categories
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getTopSpendingCategories(monthId: String, limit: Int = 3): List<CategorySummary> {
        return getCategorySummaries(monthId).take(limit)
    }

    /**
     * Get daily spending for last N days (for trend chart)
     */

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getDailySpendingTrend(monthId: String, days: Int = 7): List<Pair<String, Double>> {
        val today = LocalDate.now()
        val results = mutableListOf<Pair<String, Double>>()

        for (i in (days - 1) downTo 0) {
            val date = today.minusDays(i.toLong())
            val dateString = date.toString()
            val spending = expenseDao.getTotalSpendingForPeriod(dateString, dateString) ?: 0.0
            results.add(dateString to spending)
        }

        return results
    }

    /**
     * Compare current month with previous month
     */

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun compareWithPreviousMonth(monthId: String): MonthComparison {
        val currentOverview = getMonthlyOverview(monthId)

        val yearMonth = YearMonth.parse(monthId, DateTimeFormatter.ofPattern("yyyy-MM"))
        val previousMonth = yearMonth.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"))

        val previousOverview = try {
            getMonthlyOverview(previousMonth)
        } catch (e: Exception) {
            null
        }

        val difference = if (previousOverview != null) {
            currentOverview.totalSpent - previousOverview.totalSpent
        } else {
            0.0
        }

        val percentageChange = if (previousOverview != null && previousOverview.totalSpent > 0) {
            ((currentOverview.totalSpent - previousOverview.totalSpent) / previousOverview.totalSpent) * 100
        } else {
            0.0
        }

        return MonthComparison(
            currentMonth = currentOverview,
            previousMonth = previousOverview,
            difference = difference,
            percentageChange = percentageChange
        )
    }
    
    /**
     * Helper to get start and end dates for a month
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getMonthDateRange(monthId: String): Pair<String, String> {
        val yearMonth = YearMonth.parse(monthId, DateTimeFormatter.ofPattern("yyyy-MM"))
        val startDate = yearMonth.atDay(1).toString()
        val endDate = yearMonth.atEndOfMonth().toString()
        return startDate to endDate
    }

    /**
     * Data class for month comparison
     */

    data class MonthComparison(
        val currentMonth: MonthlyOverview,
        val previousMonth: MonthlyOverview?,
        val difference: Double,
        val percentageChange: Double
    )
}
