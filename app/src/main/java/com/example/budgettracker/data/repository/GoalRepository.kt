package com.example.budgettracker.data.repository

import com.example.budgettracker.data.dao.GoalContributionDao
import com.example.budgettracker.data.dao.GoalDao
import com.example.budgettracker.data.entity.GoalContributionEntity
import com.example.budgettracker.data.entity.GoalEntity
import com.example.budgettracker.data.model.GoalContribution
import com.example.budgettracker.data.model.GoalContributionSourceType
import com.example.budgettracker.data.model.GoalFeasibilityStatus
import com.example.budgettracker.data.model.GoalHealthClassification
import com.example.budgettracker.data.model.GoalPlan
import com.example.budgettracker.data.model.GoalPriority
import com.example.budgettracker.data.model.GoalStatus
import com.example.budgettracker.data.model.GoalSummary
import com.example.budgettracker.data.model.GoalType
import com.example.budgettracker.utils.CurrencyUtils
import com.example.budgettracker.utils.DateUtils
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

class GoalRepository(
    private val goalDao: GoalDao,
    private val contributionDao: GoalContributionDao
) {
    suspend fun getSummary(asOfDate: LocalDate = LocalDate.now()): GoalSummary {
        val plans = goalDao.getAllGoals().map { it.toPlan(asOfDate) }
        val active = plans.filter { it.status == GoalStatus.ACTIVE }
        val completed = plans.filter { it.status == GoalStatus.COMPLETED }

        return GoalSummary(
            activeGoals = active,
            completedGoals = completed,
            totalTargetAmount = active.sumOf { it.targetAmount },
            totalCurrentAmount = active.sumOf { it.currentAmount },
            totalRemainingAmount = active.sumOf { it.remainingAmount },
            guidance = summaryGuidance(active)
        )
    }

    suspend fun addGoal(
        name: String,
        goalType: GoalType,
        targetAmount: Double,
        currentAmount: Double,
        targetDate: String?,
        monthlyContributionTarget: Double,
        priority: GoalPriority,
        notes: String
    ): Long {
        require(name.isNotBlank()) { "Goal name is required" }
        require(targetAmount > 0.0) { "Target amount must be greater than zero" }
        require(currentAmount >= 0.0) { "Current amount cannot be negative" }
        require(monthlyContributionTarget >= 0.0) { "Monthly contribution target cannot be negative" }

        val now = System.currentTimeMillis()
        val clampedCurrent = currentAmount.coerceAtMost(targetAmount)
        return goalDao.insertGoal(
            GoalEntity(
                name = name.trim(),
                goalType = goalType.name,
                targetAmount = targetAmount,
                currentAmount = clampedCurrent,
                targetDate = targetDate?.takeIf { it.isNotBlank() },
                monthlyContributionTarget = monthlyContributionTarget,
                priority = priority.name,
                healthClassification = healthFor(goalType).name,
                status = if (clampedCurrent >= targetAmount) GoalStatus.COMPLETED.name else GoalStatus.ACTIVE.name,
                notes = notes.trim(),
                createdAt = now,
                updatedAt = now,
                completedAt = if (clampedCurrent >= targetAmount) DateUtils.getCurrentDate() else null
            )
        )
    }

    suspend fun addContribution(
        goalId: Long,
        amount: Double,
        contributionDate: String = DateUtils.getCurrentDate(),
        sourceType: GoalContributionSourceType,
        linkedExtraIncomeId: Long? = null,
        notes: String = ""
    ): Long {
        require(amount > 0.0) { "Contribution amount must be greater than zero" }
        val goal = goalDao.getGoalById(goalId) ?: error("Goal not found")
        require(goal.status != GoalStatus.CANCELLED.name) { "Cancelled goals cannot receive contributions" }

        val now = System.currentTimeMillis()
        val id = contributionDao.insertContribution(
            GoalContributionEntity(
                goalId = goalId,
                amount = amount,
                contributionDate = contributionDate,
                sourceType = sourceType.name,
                linkedExtraIncomeId = linkedExtraIncomeId,
                notes = notes.trim(),
                createdAt = now,
                updatedAt = now
            )
        )

        val newCurrent = (goal.currentAmount + amount).coerceAtMost(goal.targetAmount)
        val completed = newCurrent >= goal.targetAmount
        goalDao.updateGoal(
            goal.copy(
                currentAmount = newCurrent,
                status = if (completed) GoalStatus.COMPLETED.name else goal.status,
                completedAt = if (completed && goal.completedAt == null) contributionDate else goal.completedAt,
                updatedAt = now
            )
        )
        return id
    }

    suspend fun getContributions(goalId: Long): List<GoalContribution> {
        return contributionDao.getContributionsForGoal(goalId).map { it.toModel() }
    }

    private fun GoalEntity.toPlan(asOfDate: LocalDate): GoalPlan {
        val remaining = (targetAmount - currentAmount).coerceAtLeast(0.0)
        val progress = if (targetAmount > 0.0) (currentAmount / targetAmount) * 100.0 else 100.0
        val target = targetDate?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
        val months = target?.let { monthsUntil(asOfDate, it) }
        val required = months?.let {
            if (remaining <= 0.0) 0.0 else remaining / it.coerceAtLeast(1)
        }
        val feasibility = feasibilityFor(remaining, target, months, required, monthlyContributionTarget, status, asOfDate)

        return GoalPlan(
            goalId = id,
            name = name,
            goalType = parseGoalType(goalType),
            targetAmount = targetAmount,
            currentAmount = currentAmount,
            remainingAmount = remaining,
            progressPercentage = progress.coerceIn(0.0, 100.0),
            targetDate = targetDate,
            monthsRemaining = months,
            requiredMonthlyContribution = required,
            monthlyContributionTarget = monthlyContributionTarget,
            priority = parsePriority(priority),
            healthClassification = parseHealth(healthClassification),
            feasibilityStatus = feasibility,
            status = parseStatus(status),
            guidance = guidanceFor(remaining, required, monthlyContributionTarget, target, feasibility),
            notes = notes
        )
    }

    private fun monthsUntil(asOfDate: LocalDate, targetDate: LocalDate): Int {
        return ChronoUnit.MONTHS.between(
            YearMonth.from(asOfDate),
            YearMonth.from(targetDate)
        ).toInt().coerceAtLeast(1)
    }

    private fun feasibilityFor(
        remaining: Double,
        targetDate: LocalDate?,
        monthsRemaining: Int?,
        requiredMonthlyContribution: Double?,
        monthlyContributionTarget: Double,
        status: String,
        asOfDate: LocalDate
    ): GoalFeasibilityStatus {
        if (status == GoalStatus.COMPLETED.name || remaining <= 0.0) return GoalFeasibilityStatus.FEASIBLE
        if (targetDate == null || monthsRemaining == null || targetDate.isBefore(asOfDate)) {
            return GoalFeasibilityStatus.NOT_FEASIBLE
        }
        val required = requiredMonthlyContribution ?: return GoalFeasibilityStatus.NOT_FEASIBLE
        if (monthlyContributionTarget <= 0.0) return GoalFeasibilityStatus.RISKY
        return when {
            required <= monthlyContributionTarget -> GoalFeasibilityStatus.FEASIBLE
            required <= monthlyContributionTarget * 1.25 -> GoalFeasibilityStatus.TIGHT
            required <= monthlyContributionTarget * 2.0 -> GoalFeasibilityStatus.RISKY
            else -> GoalFeasibilityStatus.NOT_FEASIBLE
        }
    }

    private fun guidanceFor(
        remaining: Double,
        required: Double?,
        monthlyTarget: Double,
        targetDate: LocalDate?,
        feasibility: GoalFeasibilityStatus
    ): String {
        if (remaining <= 0.0) return "This goal is complete. That is real progress."
        if (targetDate == null) return "Add a target date to understand the monthly contribution needed."
        val requiredText = required?.let { "You would need about ${CurrencyUtils.format(it)} per month." }
            ?: "Set a target date to calculate the monthly requirement."
        return when (feasibility) {
            GoalFeasibilityStatus.FEASIBLE ->
                "This goal fits your current monthly target. $requiredText"
            GoalFeasibilityStatus.TIGHT ->
                "This goal is possible, but it will be tight. $requiredText"
            GoalFeasibilityStatus.RISKY ->
                if (monthlyTarget <= 0.0) {
                    "Set a monthly contribution target so this goal can be judged clearly."
                } else {
                    "This goal may add pressure. $requiredText Consider lowering the target or delaying the date."
                }
            GoalFeasibilityStatus.NOT_FEASIBLE ->
                "This goal does not fit the current plan yet. $requiredText You can adjust the amount, date, or monthly contribution."
        }
    }

    private fun summaryGuidance(activeGoals: List<GoalPlan>): String {
        return when {
            activeGoals.isEmpty() -> "Create a goal when you are ready to plan the next useful step."
            activeGoals.any { it.feasibilityStatus == GoalFeasibilityStatus.NOT_FEASIBLE } ->
                "Some goals need adjustment before they fit the current plan. That is planning information, not failure."
            activeGoals.any { it.feasibilityStatus == GoalFeasibilityStatus.RISKY } ->
                "A goal is adding pressure. Adjusting the date or target can protect your recovery plan."
            else -> "Your active goals have a clear path. Keep contributions realistic and consistent."
        }
    }

    private fun healthFor(goalType: GoalType): GoalHealthClassification {
        return when (goalType) {
            GoalType.EMERGENCY_FUND,
            GoalType.DEBT_PAYOFF,
            GoalType.SCHOOL_FEES,
            GoalType.INVESTMENT -> GoalHealthClassification.HEALTHY
            GoalType.HOLIDAY_TRAVEL -> GoalHealthClassification.NEUTRAL
            else -> GoalHealthClassification.NEUTRAL
        }
    }

    private fun GoalContributionEntity.toModel(): GoalContribution {
        return GoalContribution(
            contributionId = id,
            goalId = goalId,
            amount = amount,
            contributionDate = contributionDate,
            sourceType = parseContributionSource(sourceType),
            linkedExtraIncomeId = linkedExtraIncomeId,
            notes = notes
        )
    }

    private fun parseGoalType(value: String): GoalType {
        return GoalType.values().firstOrNull { it.name == value } ?: GoalType.CUSTOM
    }

    private fun parseStatus(value: String): GoalStatus {
        return GoalStatus.values().firstOrNull { it.name == value } ?: GoalStatus.ACTIVE
    }

    private fun parsePriority(value: String): GoalPriority {
        return GoalPriority.values().firstOrNull { it.name == value } ?: GoalPriority.MEDIUM
    }

    private fun parseHealth(value: String): GoalHealthClassification {
        return GoalHealthClassification.values().firstOrNull { it.name == value }
            ?: GoalHealthClassification.NEUTRAL
    }

    private fun parseContributionSource(value: String): GoalContributionSourceType {
        return GoalContributionSourceType.values().firstOrNull { it.name == value }
            ?: GoalContributionSourceType.OTHER
    }
}
