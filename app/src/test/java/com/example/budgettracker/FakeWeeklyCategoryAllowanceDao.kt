package com.example.budgettracker

import com.example.budgettracker.data.dao.WeeklyCategoryAllowanceDao
import com.example.budgettracker.data.entity.WeeklyCategoryAllowanceEntity

class FakeWeeklyCategoryAllowanceDao : WeeklyCategoryAllowanceDao {
    private val limits = mutableMapOf<Pair<String, Long>, WeeklyCategoryAllowanceEntity>()

    override suspend fun getWeekLimits(weekStartDate: String): List<WeeklyCategoryAllowanceEntity> {
        return limits.values.filter { it.weekStartDate == weekStartDate }
    }

    override suspend fun upsertLimit(limit: WeeklyCategoryAllowanceEntity) {
        limits[limit.weekStartDate to limit.categoryId] = limit
    }
}
