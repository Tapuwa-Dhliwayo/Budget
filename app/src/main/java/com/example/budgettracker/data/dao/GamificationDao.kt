package com.example.budgettracker.data.dao

import androidx.room.*
import com.example.budgettracker.data.entity.GamificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GamificationDao {
    
    @Query("SELECT * FROM gamification WHERE id = 1")
    suspend fun getGamification(): GamificationEntity?
    
    @Query("SELECT * FROM gamification WHERE id = 1")
    fun observeGamification(): Flow<GamificationEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGamification(gamification: GamificationEntity)
    
    @Update
    suspend fun updateGamification(gamification: GamificationEntity)
    
    @Query("UPDATE gamification SET currentStreak = :streak, lastLoggedDate = :date WHERE id = 1")
    suspend fun updateStreak(streak: Int, date: String)
    
    @Query("UPDATE gamification SET longestStreak = :streak WHERE id = 1")
    suspend fun updateLongestStreak(streak: Int)
}
