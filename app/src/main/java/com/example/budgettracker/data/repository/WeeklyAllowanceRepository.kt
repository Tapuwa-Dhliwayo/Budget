package com.example.budgettracker.data.repository

import com.example.budgettracker.data.dao.CategoryDao
import com.example.budgettracker.data.dao.ExpenseDao
import com.example.budgettracker.data.dao.WeeklyAllowanceDao
import com.example.budgettracker.data.dao.WeeklyCategoryAllowanceDao
import com.example.budgettracker.data.dao.WeeklyRecoveryActionDao
import com.example.budgettracker.data.dao.WeeklyReviewDao
import com.example.budgettracker.data.entity.WeeklyCategoryAllowanceEntity
import com.example.budgettracker.data.entity.WeeklyAllowanceEntity
import com.example.budgettracker.data.entity.WeeklyRecoveryActionEntity
import com.example.budgettracker.data.entity.WeeklyReviewEntity
import com.example.budgettracker.data.model.WeeklyCategoryPressure
import com.example.budgettracker.data.model.WeeklyRecoveryAction
import com.example.budgettracker.data.model.WeeklyAllowanceStatus
import com.example.budgettracker.data.model.WeeklyAllowanceSummary
import com.example.budgettracker.data.model.WeeklyReview
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

class WeeklyAllowanceRepository(
    private val weeklyAllowanceDao: WeeklyAllowanceDao,
    private val weeklyReviewDao: WeeklyReviewDao,
    private val weeklyCategoryAllowanceDao: WeeklyCategoryAllowanceDao,
    private val weeklyRecoveryActionDao: WeeklyRecoveryActionDao,
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao
) {

    suspend fun getCurrentWeekSummary(today: LocalDate = LocalDate.now()): WeeklyAllowanceSummary {
        val weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        return getWeekSummary(weekStart, today)
    }

    suspend fun getWeekSummary(
        weekStart: LocalDate,
        today: LocalDate = LocalDate.now()
    ): WeeklyAllowanceSummary {
        val weekEnd = weekStart.plusDays(6)
        val allowance = weeklyAllowanceDao.getWeek(weekStart.toString())
        val weekExpenses = expenseDao.getNonRecurringExpensesForPeriod(weekStart.toString(), weekEnd.toString())
        val spent = weekExpenses.sumOf { it.amount }
        val categoryLimits = weeklyCategoryAllowanceDao.getWeekLimits(weekStart.toString())
            .associateBy { it.categoryId }
        val pressures = getCategoryPressures(weekExpenses, spent, categoryLimits)
        val review = weeklyReviewDao.getReview(weekStart.toString())?.toModel()
        val completedActions = weeklyRecoveryActionDao.getWeekActions(weekStart.toString())
            .associateBy { it.actionText }

        if (allowance == null) {
            return WeeklyAllowanceSummary(
                weekStartDate = weekStart.toString(),
                weekEndDate = weekEnd.toString(),
                allowanceSet = false,
                allowanceAmount = 0.0,
                spent = spent,
                remaining = -spent,
                safeDailySpend = 0.0,
                daysRemaining = daysRemaining(today, weekEnd),
                status = WeeklyAllowanceStatus.NOT_SET,
                guidance = "Set a weekly allowance to see your spending pressure for this week.",
                categoryPressures = pressures,
                recoveryActions = listOf(
                    WeeklyRecoveryAction(
                        actionText = "Set a weekly allowance for this week so your spending has a clear target.",
                        isCompleted = completedActions["Set a weekly allowance for this week so your spending has a clear target."]?.isCompleted ?: false
                    )
                ),
                review = review
            )
        }

        return buildSummary(allowance, spent, today, pressures, review, completedActions)
    }

    suspend fun setCurrentWeekAllowance(
        amount: Double,
        today: LocalDate = LocalDate.now()
    ) {
        require(amount >= 0.0) { "Weekly allowance cannot be negative" }

        val weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val existing = weeklyAllowanceDao.getWeek(weekStart.toString())
        val now = System.currentTimeMillis()
        val allowance = WeeklyAllowanceEntity(
            weekStartDate = weekStart.toString(),
            weekEndDate = weekStart.plusDays(6).toString(),
            allowanceAmount = amount,
            createdAt = existing?.createdAt ?: now,
            updatedAt = now
        )

        weeklyAllowanceDao.upsertWeek(allowance)
    }

    suspend fun saveCurrentWeekReview(
        wentWell: String,
        challenge: String,
        nextWeekAdjustment: String,
        today: LocalDate = LocalDate.now()
    ) {
        val weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val existing = weeklyReviewDao.getReview(weekStart.toString())
        val now = System.currentTimeMillis()
        weeklyReviewDao.upsertReview(
            WeeklyReviewEntity(
                weekStartDate = weekStart.toString(),
                weekEndDate = weekStart.plusDays(6).toString(),
                wentWell = wentWell.trim(),
                challenge = challenge.trim(),
                nextWeekAdjustment = nextWeekAdjustment.trim(),
                createdAt = existing?.createdAt ?: now,
                updatedAt = now
            )
        )
    }

    suspend fun setCurrentWeekCategoryLimit(
        categoryId: Long,
        limitAmount: Double,
        today: LocalDate = LocalDate.now()
    ) {
        require(limitAmount >= 0.0) { "Category limit cannot be negative" }

        val weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        weeklyCategoryAllowanceDao.upsertLimit(
            WeeklyCategoryAllowanceEntity(
                weekStartDate = weekStart.toString(),
                weekEndDate = weekStart.plusDays(6).toString(),
                categoryId = categoryId,
                limitAmount = limitAmount
            )
        )
    }

    suspend fun setCurrentWeekRecoveryActionCompleted(
        actionText: String,
        isCompleted: Boolean,
        today: LocalDate = LocalDate.now()
    ) {
        val weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        weeklyRecoveryActionDao.upsertAction(
            WeeklyRecoveryActionEntity(
                weekStartDate = weekStart.toString(),
                weekEndDate = weekStart.plusDays(6).toString(),
                actionText = actionText,
                isCompleted = isCompleted
            )
        )
    }

    suspend fun getRecentWeekSummaries(
        limit: Int = 4,
        today: LocalDate = LocalDate.now()
    ): List<WeeklyAllowanceSummary> {
        return weeklyAllowanceDao.getRecentWeeks(limit).map { allowance ->
            val weekStart = LocalDate.parse(allowance.weekStartDate)
            val summaryDate = if (today in weekStart..weekStart.plusDays(6)) today else weekStart.plusDays(6)
            val weekExpenses = expenseDao.getNonRecurringExpensesForPeriod(
                allowance.weekStartDate,
                allowance.weekEndDate
            )
            val spent = weekExpenses.sumOf { it.amount }
            val categoryLimits = weeklyCategoryAllowanceDao.getWeekLimits(allowance.weekStartDate)
                .associateBy { it.categoryId }
            buildSummary(
                allowance = allowance,
                spent = spent,
                today = summaryDate,
                categoryPressures = getCategoryPressures(weekExpenses, spent, categoryLimits),
                review = weeklyReviewDao.getReview(allowance.weekStartDate)?.toModel(),
                completedActions = weeklyRecoveryActionDao.getWeekActions(allowance.weekStartDate)
                    .associateBy { it.actionText }
            )
        }
    }

    private fun buildSummary(
        allowance: WeeklyAllowanceEntity,
        spent: Double,
        today: LocalDate,
        categoryPressures: List<WeeklyCategoryPressure>,
        review: WeeklyReview?,
        completedActions: Map<String, WeeklyRecoveryActionEntity> = emptyMap()
    ): WeeklyAllowanceSummary {
        val weekStart = LocalDate.parse(allowance.weekStartDate)
        val weekEnd = LocalDate.parse(allowance.weekEndDate)
        val daysRemaining = daysRemaining(today, weekEnd)
        val remaining = allowance.allowanceAmount - spent
        val safeDailySpend = if (remaining > 0) remaining / daysRemaining else 0.0
        val status = calculateStatus(
            allowanceAmount = allowance.allowanceAmount,
            spent = spent,
            weekStart = weekStart,
            today = today,
            remaining = remaining
        )

        return WeeklyAllowanceSummary(
            weekStartDate = allowance.weekStartDate,
            weekEndDate = allowance.weekEndDate,
            allowanceSet = true,
            allowanceAmount = allowance.allowanceAmount,
            spent = spent,
            remaining = remaining,
            safeDailySpend = safeDailySpend,
            daysRemaining = daysRemaining,
            status = status,
            guidance = guidanceFor(status),
            categoryPressures = categoryPressures,
            recoveryActions = recoveryActionsFor(status, categoryPressures, completedActions),
            review = review
        )
    }

    private suspend fun getCategoryPressures(
        expenses: List<com.example.budgettracker.data.entity.ExpenseEntity>,
        totalSpent: Double,
        categoryLimits: Map<Long, WeeklyCategoryAllowanceEntity>
    ): List<WeeklyCategoryPressure> {
        val categoriesById = categoryDao.getAllCategories().associateBy { it.id }
        val spentPressures = expenses
            .groupBy { it.categoryId }
            .mapNotNull { (categoryId, categoryExpenses) ->
                val category = categoriesById[categoryId] ?: return@mapNotNull null
                val amount = categoryExpenses.sumOf { it.amount }
                val limit = categoryLimits[categoryId]?.limitAmount
                WeeklyCategoryPressure(
                    categoryId = categoryId,
                    categoryName = category.name,
                    categoryIcon = category.icon,
                    amount = amount,
                    percentageOfWeekSpend = if (totalSpent > 0.0) (amount / totalSpent) * 100.0 else 0.0,
                    weeklyLimit = limit,
                    limitRemaining = limit?.minus(amount),
                    isOverLimit = limit != null && amount > limit
                )
            }

        val categoriesWithOnlyLimits = categoryLimits.values
            .filter { limit -> spentPressures.none { it.categoryId == limit.categoryId } }
            .mapNotNull { limit ->
                val category = categoriesById[limit.categoryId] ?: return@mapNotNull null
                WeeklyCategoryPressure(
                    categoryId = category.id,
                    categoryName = category.name,
                    categoryIcon = category.icon,
                    amount = 0.0,
                    percentageOfWeekSpend = 0.0,
                    weeklyLimit = limit.limitAmount,
                    limitRemaining = limit.limitAmount,
                    isOverLimit = false
                )
            }

        return (spentPressures + categoriesWithOnlyLimits).sortedWith(
            compareByDescending<WeeklyCategoryPressure> { it.isOverLimit }
                .thenByDescending { it.amount }
        )
    }

    private fun calculateStatus(
        allowanceAmount: Double,
        spent: Double,
        weekStart: LocalDate,
        today: LocalDate,
        remaining: Double
    ): WeeklyAllowanceStatus {
        if (allowanceAmount <= 0.0) {
            return if (spent > 0.0) WeeklyAllowanceStatus.OVER_PLAN else WeeklyAllowanceStatus.STABLE
        }
        if (spent > allowanceAmount) return WeeklyAllowanceStatus.OVER_PLAN

        val elapsedDays = ChronoUnit.DAYS.between(weekStart, today).toInt()
            .coerceIn(0, 6) + 1
        val expectedSpend = allowanceAmount * (elapsedDays / 7.0)
        val paceRatio = if (expectedSpend > 0.0) spent / expectedSpend else 0.0
        val remainingRatio = remaining / allowanceAmount

        return when {
            remainingRatio <= 0.10 -> WeeklyAllowanceStatus.CRITICAL
            paceRatio > 1.35 -> WeeklyAllowanceStatus.PRESSURED
            paceRatio > 1.10 -> WeeklyAllowanceStatus.WATCHFUL
            else -> WeeklyAllowanceStatus.STABLE
        }
    }

    private fun daysRemaining(today: LocalDate, weekEnd: LocalDate): Int {
        return (ChronoUnit.DAYS.between(today, weekEnd).toInt() + 1).coerceAtLeast(1)
    }

    private fun guidanceFor(status: WeeklyAllowanceStatus): String {
        return when (status) {
            WeeklyAllowanceStatus.NOT_SET ->
                "Set a weekly allowance to see your spending pressure for this week."
            WeeklyAllowanceStatus.STABLE ->
                "You are on track for the week. Keep logging honestly."
            WeeklyAllowanceStatus.WATCHFUL ->
                "Spending is a little ahead of pace. A small adjustment can keep the week comfortable."
            WeeklyAllowanceStatus.PRESSURED ->
                "This week is under pressure. Focus on essentials and check which categories are driving spend."
            WeeklyAllowanceStatus.CRITICAL ->
                "There is very little room left this week. You can still recover by slowing spend now."
            WeeklyAllowanceStatus.OVER_PLAN ->
                "This week is over plan, but it is not wasted. Because you logged honestly, you can plan the next step."
        }
    }

    private fun recoveryActionsFor(
        status: WeeklyAllowanceStatus,
        categoryPressures: List<WeeklyCategoryPressure>,
        completedActions: Map<String, WeeklyRecoveryActionEntity>
    ): List<WeeklyRecoveryAction> {
        val mainPressure = categoryPressures.firstOrNull { it.amount > 0.0 }
        val actions = when (status) {
            WeeklyAllowanceStatus.NOT_SET -> listOf(
                "Set this week's allowance so you can see pressure early."
            )
            WeeklyAllowanceStatus.STABLE -> listOf(
                "Keep logging expenses honestly.",
                "Protect the current pace by checking in before larger purchases."
            )
            WeeklyAllowanceStatus.WATCHFUL -> listOfNotNull(
                "Slow non-essential spending for the next day or two.",
                mainPressure?.let { "Review ${it.categoryName}; it is the biggest pressure point this week." }
            )
            WeeklyAllowanceStatus.PRESSURED -> listOfNotNull(
                "Pause flexible spending until the safe daily amount feels comfortable again.",
                mainPressure?.let { "Look at ${it.categoryName} first; reducing it can create the quickest relief." },
                "Move any non-essential planned purchase to next week."
            )
            WeeklyAllowanceStatus.CRITICAL -> listOfNotNull(
                "Focus on essentials only for the rest of the week.",
                mainPressure?.let { "Check ${it.categoryName}; it is using the largest share of weekly spend." },
                "Write a short review note so next week's plan is easier."
            )
            WeeklyAllowanceStatus.OVER_PLAN -> listOfNotNull(
                "Keep logging honestly; the week still gives useful information.",
                mainPressure?.let { "Use ${it.categoryName} as the first category to adjust next week." },
                "Complete a weekly review and choose one small adjustment for next week."
            )
        }

        return actions.map { action ->
            WeeklyRecoveryAction(
                actionText = action,
                isCompleted = completedActions[action]?.isCompleted ?: false
            )
        }
    }

    private fun WeeklyReviewEntity.toModel(): WeeklyReview {
        return WeeklyReview(
            weekStartDate = weekStartDate,
            weekEndDate = weekEndDate,
            wentWell = wentWell,
            challenge = challenge,
            nextWeekAdjustment = nextWeekAdjustment
        )
    }
}
