package com.example.budgettracker

import com.example.budgettracker.data.dao.WeeklyRecoveryActionDao
import com.example.budgettracker.data.entity.WeeklyRecoveryActionEntity

class FakeWeeklyRecoveryActionDao : WeeklyRecoveryActionDao {
    private val actions = mutableMapOf<Pair<String, String>, WeeklyRecoveryActionEntity>()

    override suspend fun getWeekActions(weekStartDate: String): List<WeeklyRecoveryActionEntity> {
        return actions.values.filter { it.weekStartDate == weekStartDate }
    }

    override suspend fun upsertAction(action: WeeklyRecoveryActionEntity) {
        actions[action.weekStartDate to action.actionText] = action
    }
}
