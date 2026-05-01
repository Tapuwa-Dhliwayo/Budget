package com.example.budgettracker

import com.example.budgettracker.data.dao.GoalContributionDao
import com.example.budgettracker.data.entity.GoalContributionEntity

class FakeGoalContributionDao : GoalContributionDao {
    private val contributions = mutableListOf<GoalContributionEntity>()
    private var nextId = 1L

    override suspend fun getContributionsForGoal(goalId: Long): List<GoalContributionEntity> {
        return contributions
            .filter { it.goalId == goalId }
            .sortedWith(compareByDescending<GoalContributionEntity> { it.contributionDate }.thenByDescending { it.id })
    }

    override suspend fun getRecentContributions(limit: Int): List<GoalContributionEntity> {
        return contributions
            .sortedWith(compareByDescending<GoalContributionEntity> { it.contributionDate }.thenByDescending { it.id })
            .take(limit)
    }

    override suspend fun insertContribution(contribution: GoalContributionEntity): Long {
        val id = nextId++
        contributions.add(contribution.copy(id = id))
        return id
    }
}
