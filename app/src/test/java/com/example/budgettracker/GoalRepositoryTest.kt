package com.example.budgettracker

import com.example.budgettracker.data.model.GoalContributionSourceType
import com.example.budgettracker.data.model.GoalFeasibilityStatus
import com.example.budgettracker.data.model.GoalPriority
import com.example.budgettracker.data.model.GoalStatus
import com.example.budgettracker.data.model.GoalType
import com.example.budgettracker.data.repository.GoalRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class GoalRepositoryTest {

    private lateinit var repository: GoalRepository

    @Before
    fun setup() {
        repository = GoalRepository(FakeGoalDao(), FakeGoalContributionDao())
    }

    @Test
    fun addGoal_calculatesFeasibleMonthlyRequirement() = runTest {
        repository.addGoal(
            name = "Emergency Fund",
            goalType = GoalType.EMERGENCY_FUND,
            targetAmount = 12_000.0,
            currentAmount = 2_000.0,
            targetDate = "2026-11-01",
            monthlyContributionTarget = 2_000.0,
            priority = GoalPriority.HIGH,
            notes = ""
        )

        val goal = repository.getSummary(LocalDate.parse("2026-05-01")).activeGoals.first()

        assertEquals(10_000.0, goal.remainingAmount, 0.01)
        assertEquals(6, goal.monthsRemaining)
        assertEquals(1_666.67, goal.requiredMonthlyContribution ?: 0.0, 0.1)
        assertEquals(GoalFeasibilityStatus.FEASIBLE, goal.feasibilityStatus)
    }

    @Test
    fun addGoal_marksUnrealisticPlanAsNotFeasible() = runTest {
        repository.addGoal(
            name = "Holiday",
            goalType = GoalType.HOLIDAY_TRAVEL,
            targetAmount = 12_000.0,
            currentAmount = 0.0,
            targetDate = "2026-07-01",
            monthlyContributionTarget = 1_000.0,
            priority = GoalPriority.LOW,
            notes = ""
        )

        val goal = repository.getSummary(LocalDate.parse("2026-05-01")).activeGoals.first()

        assertEquals(GoalFeasibilityStatus.NOT_FEASIBLE, goal.feasibilityStatus)
        assertTrue(goal.guidance.contains("adjust"))
    }

    @Test
    fun addContribution_updatesProgressAndCompletesGoal() = runTest {
        val goalId = repository.addGoal(
            name = "Phone replacement",
            goalType = GoalType.ESSENTIAL_ITEM,
            targetAmount = 3_000.0,
            currentAmount = 2_500.0,
            targetDate = "2026-12-01",
            monthlyContributionTarget = 300.0,
            priority = GoalPriority.MEDIUM,
            notes = ""
        )

        repository.addContribution(
            goalId = goalId,
            amount = 500.0,
            contributionDate = "2026-05-01",
            sourceType = GoalContributionSourceType.EXTRA_INCOME,
            notes = "Bonus"
        )

        val summary = repository.getSummary(LocalDate.parse("2026-05-01"))

        assertEquals(0, summary.activeGoals.size)
        assertEquals(1, summary.completedGoals.size)
        assertEquals(GoalStatus.COMPLETED, summary.completedGoals.first().status)
        assertEquals(100.0, summary.completedGoals.first().progressPercentage, 0.01)
    }
}
