package com.example.budgettracker

import com.example.budgettracker.data.dao.WeeklyAllowanceDao
import com.example.budgettracker.data.entity.WeeklyAllowanceEntity

class FakeWeeklyAllowanceDao : WeeklyAllowanceDao {
    private val weeks = mutableMapOf<String, WeeklyAllowanceEntity>()

    override suspend fun getWeek(weekStartDate: String): WeeklyAllowanceEntity? {
        return weeks[weekStartDate]
    }

    override suspend fun getRecentWeeks(limit: Int): List<WeeklyAllowanceEntity> {
        return weeks.values.sortedByDescending { it.weekStartDate }.take(limit)
    }

    override suspend fun upsertWeek(allowance: WeeklyAllowanceEntity) {
        weeks[allowance.weekStartDate] = allowance
    }

    fun clear() {
        weeks.clear()
    }
}
