package com.example.budgettracker

import com.example.budgettracker.data.dao.GamificationEventDao
import com.example.budgettracker.data.entity.GamificationEventEntity

class FakeGamificationEventDao : GamificationEventDao {
    private val events = mutableListOf<GamificationEventEntity>()
    private var nextId = 1L

    override suspend fun getRecentEvents(limit: Int): List<GamificationEventEntity> {
        return events
            .sortedWith(compareByDescending<GamificationEventEntity> { it.occurredDate }.thenByDescending { it.id })
            .take(limit)
    }

    override suspend fun getTotalEventXp(): Int {
        return events.sumOf { it.xpEarned }
    }

    override suspend fun insertEvent(event: GamificationEventEntity): Long {
        val id = nextId++
        events.add(event.copy(id = id))
        return id
    }
}
