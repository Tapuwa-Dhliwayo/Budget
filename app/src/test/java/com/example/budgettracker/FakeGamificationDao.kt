package com.example.budgettracker

import com.example.budgettracker.data.dao.GamificationDao
import com.example.budgettracker.data.entity.GamificationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeGamificationDao : GamificationDao {

    private var gamification: GamificationEntity? = null
    private val flowData = MutableStateFlow<GamificationEntity?>(null)

    override suspend fun getGamification(): GamificationEntity? = gamification

    override fun observeGamification(): Flow<GamificationEntity?> = flowData

    override suspend fun insertGamification(gamification: GamificationEntity) {
        this.gamification = gamification
        flowData.value = gamification
    }

    override suspend fun updateGamification(gamification: GamificationEntity) {
        this.gamification = gamification
        flowData.value = gamification
    }

    override suspend fun updateStreak(streak: Int, date: String) {
        gamification?.let {
            val updated = it.copy(currentStreak = streak, lastLoggedDate = date)
            this.gamification = updated
            flowData.value = updated
        }
    }

    override suspend fun updateLongestStreak(streak: Int) {
        gamification?.let {
            val updated = it.copy(longestStreak = streak)
            this.gamification = updated
            flowData.value = updated
        }
    }

    fun clear() {
        gamification = null
        flowData.value = null
    }
}