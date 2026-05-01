package com.example.budgettracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.budgettracker.data.entity.GamificationEventEntity

@Dao
interface GamificationEventDao {
    @Query("SELECT * FROM gamification_events ORDER BY occurredDate DESC, id DESC LIMIT :limit")
    suspend fun getRecentEvents(limit: Int): List<GamificationEventEntity>

    @Query("SELECT COALESCE(SUM(xpEarned), 0) FROM gamification_events")
    suspend fun getTotalEventXp(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: GamificationEventEntity): Long
}
