package com.example.budgettracker

import com.example.budgettracker.data.dao.GoalDao
import com.example.budgettracker.data.entity.GoalEntity

class FakeGoalDao : GoalDao {
    private val goals = mutableListOf<GoalEntity>()
    private var nextId = 1L

    override suspend fun getAllGoals(): List<GoalEntity> {
        return goals.toList()
    }

    override suspend fun getActiveGoals(): List<GoalEntity> {
        return goals.filter { it.status == "ACTIVE" }
    }

    override suspend fun getGoalById(id: Long): GoalEntity? {
        return goals.find { it.id == id }
    }

    override suspend fun insertGoal(goal: GoalEntity): Long {
        val id = nextId++
        goals.add(goal.copy(id = id))
        return id
    }

    override suspend fun updateGoal(goal: GoalEntity) {
        val index = goals.indexOfFirst { it.id == goal.id }
        if (index >= 0) {
            goals[index] = goal
        }
    }
}
