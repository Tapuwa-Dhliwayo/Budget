package com.example.budgettracker.data.model

/**
 * Represents category spending with budget comparison
 */
data class CategorySpending(
    val categoryId: Long,
    val categoryName: String,
    val categoryColor: Int,
    val categoryIcon: String,
    val totalSpent: Double,
    val budgetLimit: Double,
    val percentageUsed: Double,
    val remaining: Double,
    val isOverBudget: Boolean
)

/**
 * Monthly overview statistics
 */
data class MonthlyOverview(
    val monthId: String,
    val totalBudget: Double,
    val totalSpent: Double,
    val remaining: Double,
    val percentageUsed: Double,
    val isOverBudget: Boolean
)

/**
 * Expense with category details for display
 */
data class ExpenseWithCategory(
    val expenseId: Long,
    val amount: Double,
    val description: String,
    val date: String,
    val photoPath: String?,
    val categoryName: String,
    val categoryColor: Int,
    val categoryIcon: String
)

/**
 * Category summary for text-based analytics
 */
data class CategorySummary(
    val categoryName: String,
    val categoryIcon: String,
    val amount: Double,
    val percentage: Double,
    val color: Int
)

/**
 * Gamification status
 */
data class GamificationStatus(
    val currentStreak: Int,
    val longestStreak: Int,
    val badgesEarned: List<Badge>,
    val totalExpenses: Int
)

/**
 * Badge definition
 */
enum class Badge(val id: String, val title: String, val description: String) {
    FIRST_ENTRY("first_entry", "First Step", "Logged your first expense"),
    WEEK_WARRIOR("week_warrior", "Week Warrior", "7-day logging streak"),
    BUDGET_HERO("budget_hero", "Budget Hero", "Stayed under budget for a month"),
    STREAK_MASTER("streak_master", "Streak Master", "30-day logging streak")
}
